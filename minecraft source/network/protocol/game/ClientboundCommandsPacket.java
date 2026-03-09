/*     */ package net.minecraft.network.protocol.game;
/*     */ 
/*     */ import com.mojang.brigadier.arguments.ArgumentType;
/*     */ import com.mojang.brigadier.builder.ArgumentBuilder;
/*     */ import com.mojang.brigadier.tree.ArgumentCommandNode;
/*     */ import com.mojang.brigadier.tree.CommandNode;
/*     */ import com.mojang.brigadier.tree.RootCommandNode;
/*     */ import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
/*     */ import it.unimi.dsi.fastutil.ints.IntSet;
/*     */ import it.unimi.dsi.fastutil.ints.IntSets;
/*     */ import it.unimi.dsi.fastutil.objects.Object2IntMap;
/*     */ import it.unimi.dsi.fastutil.objects.Object2IntMaps;
/*     */ import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectArrayList;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectIterator;
/*     */ import java.util.ArrayDeque;
/*     */ import java.util.List;
/*     */ import java.util.Queue;
/*     */ import java.util.function.BiPredicate;
/*     */ import net.minecraft.commands.CommandBuildContext;
/*     */ import net.minecraft.commands.synchronization.ArgumentTypeInfo;
/*     */ import net.minecraft.core.registries.BuiltInRegistries;
/*     */ import net.minecraft.network.FriendlyByteBuf;
/*     */ import net.minecraft.network.PacketListener;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ import net.minecraft.network.codec.StreamDecoder;
/*     */ import net.minecraft.network.protocol.Packet;
/*     */ import net.minecraft.network.protocol.PacketType;
/*     */ import net.minecraft.resources.Identifier;
/*     */ 
/*     */ public class ClientboundCommandsPacket
/*     */   extends Object implements Packet<ClientGamePacketListener> {
/*  33 */   public static final StreamCodec<FriendlyByteBuf, ClientboundCommandsPacket> STREAM_CODEC = Packet.codec(ClientboundCommandsPacket::write, ClientboundCommandsPacket::new);
/*     */   
/*     */   private static final byte MASK_TYPE = 3;
/*     */   
/*     */   private static final byte FLAG_EXECUTABLE = 4;
/*     */   
/*     */   private static final byte FLAG_REDIRECT = 8;
/*     */   private static final byte FLAG_CUSTOM_SUGGESTIONS = 16;
/*     */   private static final byte FLAG_RESTRICTED = 32;
/*     */   private static final byte TYPE_ROOT = 0;
/*     */   private static final byte TYPE_LITERAL = 1;
/*     */   private static final byte TYPE_ARGUMENT = 2;
/*     */   private final int rootIndex;
/*     */   private final List<Entry> entries;
/*     */   
/*     */   public <S> ClientboundCommandsPacket(RootCommandNode<S> root, NodeInspector<S> inspector) {
/*  49 */     Object2IntMap<CommandNode<S>> nodeToId = enumerateNodes(root);
/*  50 */     this.entries = createEntries(nodeToId, inspector);
/*  51 */     this.rootIndex = nodeToId.getInt(root);
/*     */   }
/*     */   
/*     */   private ClientboundCommandsPacket(FriendlyByteBuf input) {
/*  55 */     this.entries = input.readList(ClientboundCommandsPacket::readNode);
/*  56 */     this.rootIndex = input.readVarInt();
/*  57 */     validateEntries(this.entries);
/*     */   }
/*     */   
/*     */   private void write(FriendlyByteBuf output) {
/*  61 */     output.writeCollection(this.entries, (buffer, entry) -> entry.write(buffer));
/*  62 */     output.writeVarInt(this.rootIndex);
/*     */   }
/*     */   
/*     */   private static void validateEntries(List<Entry> entries, BiPredicate<Entry, IntSet> validator) {
/*  66 */     IntOpenHashSet intOpenHashSet = new IntOpenHashSet(IntSets.fromTo(0, entries.size()));
/*  67 */     while (!intOpenHashSet.isEmpty()) {
/*  68 */       boolean worked = intOpenHashSet.removeIf(index -> validator.test((Entry)entries.get(index), elementsToCheck));
/*  69 */       if (!worked) {
/*  70 */         throw new IllegalStateException("Server sent an impossible command tree");
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   private static void validateEntries(List<Entry> entries) {
/*  76 */     validateEntries(entries, Entry::canBuild);
/*  77 */     validateEntries(entries, Entry::canResolve);
/*     */   }
/*     */   
/*     */   private static <S> Object2IntMap<CommandNode<S>> enumerateNodes(RootCommandNode<S> root) {
/*  81 */     Object2IntOpenHashMap object2IntOpenHashMap = new Object2IntOpenHashMap();
/*  82 */     Queue<CommandNode<S>> queue = new ArrayDeque<CommandNode<S>>();
/*  83 */     queue.add(root);
/*     */     
/*     */     CommandNode<S> node;
/*  86 */     while ((node = (CommandNode)queue.poll()) != null) {
/*  87 */       if (object2IntOpenHashMap.containsKey(node)) {
/*     */         continue;
/*     */       }
/*  90 */       int id = object2IntOpenHashMap.size();
/*  91 */       object2IntOpenHashMap.put(node, id);
/*  92 */       queue.addAll(node.getChildren());
/*  93 */       if (node.getRedirect() != null) {
/*  94 */         queue.add(node.getRedirect());
/*     */       }
/*     */     } 
/*  97 */     return object2IntOpenHashMap;
/*     */   }
/*     */   
/*     */   private static <S> List<Entry> createEntries(Object2IntMap<CommandNode<S>> nodeToId, NodeInspector<S> inspector) {
/* 101 */     ObjectArrayList<Entry> result = new ObjectArrayList<Entry>(nodeToId.size());
/* 102 */     result.size(nodeToId.size());
/* 103 */     for (ObjectIterator objectIterator = Object2IntMaps.fastIterable(nodeToId).iterator(); objectIterator.hasNext(); ) { Object2IntMap.Entry<CommandNode<S>> entry = (Object2IntMap.Entry)objectIterator.next();
/* 104 */       result.set(entry.getIntValue(), createEntry((CommandNode)entry.getKey(), inspector, nodeToId)); }
/*     */     
/* 106 */     return result;
/*     */   }
/*     */   
/*     */   private static Entry readNode(FriendlyByteBuf input) {
/* 110 */     byte flags = input.readByte();
/* 111 */     int[] children = input.readVarIntArray();
/* 112 */     int redirect = ((flags & 0x8) != 0) ? input.readVarInt() : 0;
/* 113 */     NodeStub stub = read(input, flags);
/* 114 */     return new Entry(stub, flags, redirect, children);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final class LiteralNodeStub
/*     */     extends Record
/*     */     implements NodeStub
/*     */   {
/*     */     private final String id;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 139 */     private LiteralNodeStub(String id) { this.id = id; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/network/protocol/game/ClientboundCommandsPacket$LiteralNodeStub;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #139	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/* 139 */       //   0	7	0	this	Lnet/minecraft/network/protocol/game/ClientboundCommandsPacket$LiteralNodeStub; } public String id() { return this.id; }
/*     */     public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/protocol/game/ClientboundCommandsPacket$LiteralNodeStub;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #139	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/network/protocol/game/ClientboundCommandsPacket$LiteralNodeStub; }
/*     */     public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/network/protocol/game/ClientboundCommandsPacket$LiteralNodeStub;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #139	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/network/protocol/game/ClientboundCommandsPacket$LiteralNodeStub;
/*     */       //   0	8	1	o	Ljava/lang/Object; }
/* 142 */     public <S> ArgumentBuilder<S, ?> build(CommandBuildContext context, ClientboundCommandsPacket.NodeBuilder<S> builder) { return builder.createLiteral(this.id); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 147 */     public void write(FriendlyByteBuf output) { output.writeUtf(this.id); } }
/*     */   private static final class ArgumentNodeStub extends Record implements NodeStub { private final String id; private final ArgumentTypeInfo.Template<?> argumentType;
/*     */     private final Identifier suggestionId;
/*     */     
/* 151 */     private ArgumentNodeStub(String id, ArgumentTypeInfo.Template<?> argumentType, Identifier suggestionId) { this.id = id; this.argumentType = argumentType; this.suggestionId = suggestionId; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/network/protocol/game/ClientboundCommandsPacket$ArgumentNodeStub;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #151	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/* 151 */       //   0	7	0	this	Lnet/minecraft/network/protocol/game/ClientboundCommandsPacket$ArgumentNodeStub; } public String id() { return this.id; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/protocol/game/ClientboundCommandsPacket$ArgumentNodeStub;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #151	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/network/protocol/game/ClientboundCommandsPacket$ArgumentNodeStub; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/network/protocol/game/ClientboundCommandsPacket$ArgumentNodeStub;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #151	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/network/protocol/game/ClientboundCommandsPacket$ArgumentNodeStub;
/* 151 */       //   0	8	1	o	Ljava/lang/Object; } public ArgumentTypeInfo.Template<?> argumentType() { return this.argumentType; } public Identifier suggestionId() { return this.suggestionId; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public <S> ArgumentBuilder<S, ?> build(CommandBuildContext context, ClientboundCommandsPacket.NodeBuilder<S> builder) {
/* 158 */       ArgumentType<?> type = this.argumentType.instantiate(context);
/* 159 */       return builder.createArgument(this.id, type, this.suggestionId);
/*     */     }
/*     */ 
/*     */     
/*     */     public void write(FriendlyByteBuf output) {
/* 164 */       output.writeUtf(this.id);
/* 165 */       serializeCap(output, this.argumentType);
/* 166 */       if (this.suggestionId != null) {
/* 167 */         output.writeIdentifier(this.suggestionId);
/*     */       }
/*     */     }
/*     */ 
/*     */     
/* 172 */     private static <A extends ArgumentType<?>> void serializeCap(FriendlyByteBuf output, ArgumentTypeInfo.Template<A> argumentType) { serializeCap(output, argumentType.type(), argumentType); }
/*     */ 
/*     */ 
/*     */     
/*     */     private static <A extends ArgumentType<?>, T extends ArgumentTypeInfo.Template<A>> void serializeCap(FriendlyByteBuf output, ArgumentTypeInfo<A, T> info, ArgumentTypeInfo.Template<A> argumentType) {
/* 177 */       output.writeVarInt(BuiltInRegistries.COMMAND_ARGUMENT_TYPE.getId(info));
/* 178 */       info.serializeToNetwork(argumentType, output);
/*     */     } }
/*     */ 
/*     */   
/*     */   private static NodeStub read(FriendlyByteBuf input, byte flags) {
/* 183 */     int type = flags & 0x3;
/* 184 */     if (type == 2) {
/* 185 */       String name = input.readUtf();
/* 186 */       int id = input.readVarInt();
/* 187 */       ArgumentTypeInfo<?, ?> argumentType = (ArgumentTypeInfo)BuiltInRegistries.COMMAND_ARGUMENT_TYPE.byId(id);
/* 188 */       if (argumentType == null) {
/* 189 */         return null;
/*     */       }
/* 191 */       ArgumentTypeInfo.Template<?> argument = argumentType.deserializeFromNetwork(input);
/* 192 */       Identifier suggestionId = ((flags & 0x10) != 0) ? input.readIdentifier() : null;
/* 193 */       return new ArgumentNodeStub(name, argument, suggestionId);
/*     */     } 
/* 195 */     if (type == 1) {
/* 196 */       String id = input.readUtf();
/* 197 */       return new LiteralNodeStub(id);
/*     */     } 
/* 199 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static <S> Entry createEntry(CommandNode<S> node, NodeInspector<S> inspector, Object2IntMap<CommandNode<S>> ids) { // Byte code:
/*     */     //   0: iconst_0
/*     */     //   1: istore_3
/*     */     //   2: aload_0
/*     */     //   3: invokevirtual getRedirect : ()Lcom/mojang/brigadier/tree/CommandNode;
/*     */     //   6: ifnull -> 29
/*     */     //   9: iload_3
/*     */     //   10: bipush #8
/*     */     //   12: ior
/*     */     //   13: istore_3
/*     */     //   14: aload_2
/*     */     //   15: aload_0
/*     */     //   16: invokevirtual getRedirect : ()Lcom/mojang/brigadier/tree/CommandNode;
/*     */     //   19: invokeinterface getInt : (Ljava/lang/Object;)I
/*     */     //   24: istore #4
/*     */     //   26: goto -> 32
/*     */     //   29: iconst_0
/*     */     //   30: istore #4
/*     */     //   32: aload_1
/*     */     //   33: aload_0
/*     */     //   34: invokeinterface isExecutable : (Lcom/mojang/brigadier/tree/CommandNode;)Z
/*     */     //   39: ifeq -> 46
/*     */     //   42: iload_3
/*     */     //   43: iconst_4
/*     */     //   44: ior
/*     */     //   45: istore_3
/*     */     //   46: aload_1
/*     */     //   47: aload_0
/*     */     //   48: invokeinterface isRestricted : (Lcom/mojang/brigadier/tree/CommandNode;)Z
/*     */     //   53: ifeq -> 61
/*     */     //   56: iload_3
/*     */     //   57: bipush #32
/*     */     //   59: ior
/*     */     //   60: istore_3
/*     */     //   61: aload_0
/*     */     //   62: dup
/*     */     //   63: invokestatic requireNonNull : (Ljava/lang/Object;)Ljava/lang/Object;
/*     */     //   66: pop
/*     */     //   67: astore #6
/*     */     //   69: iconst_0
/*     */     //   70: istore #7
/*     */     //   72: aload #6
/*     */     //   74: iload #7
/*     */     //   76: <illegal opcode> typeSwitch : (Ljava/lang/Object;I)I
/*     */     //   81: tableswitch default -> 211, 0 -> 108, 1 -> 125, 2 -> 183
/*     */     //   108: aload #6
/*     */     //   110: checkcast com/mojang/brigadier/tree/RootCommandNode
/*     */     //   113: astore #8
/*     */     //   115: iload_3
/*     */     //   116: iconst_0
/*     */     //   117: ior
/*     */     //   118: istore_3
/*     */     //   119: aconst_null
/*     */     //   120: astore #5
/*     */     //   122: goto -> 228
/*     */     //   125: aload #6
/*     */     //   127: checkcast com/mojang/brigadier/tree/ArgumentCommandNode
/*     */     //   130: astore #9
/*     */     //   132: aload_1
/*     */     //   133: aload #9
/*     */     //   135: invokeinterface suggestionId : (Lcom/mojang/brigadier/tree/ArgumentCommandNode;)Lnet/minecraft/resources/Identifier;
/*     */     //   140: astore #10
/*     */     //   142: new net/minecraft/network/protocol/game/ClientboundCommandsPacket$ArgumentNodeStub
/*     */     //   145: dup
/*     */     //   146: aload #9
/*     */     //   148: invokevirtual getName : ()Ljava/lang/String;
/*     */     //   151: aload #9
/*     */     //   153: invokevirtual getType : ()Lcom/mojang/brigadier/arguments/ArgumentType;
/*     */     //   156: invokestatic unpack : (Lcom/mojang/brigadier/arguments/ArgumentType;)Lnet/minecraft/commands/synchronization/ArgumentTypeInfo$Template;
/*     */     //   159: aload #10
/*     */     //   161: invokespecial <init> : (Ljava/lang/String;Lnet/minecraft/commands/synchronization/ArgumentTypeInfo$Template;Lnet/minecraft/resources/Identifier;)V
/*     */     //   164: astore #5
/*     */     //   166: iload_3
/*     */     //   167: iconst_2
/*     */     //   168: ior
/*     */     //   169: istore_3
/*     */     //   170: aload #10
/*     */     //   172: ifnull -> 180
/*     */     //   175: iload_3
/*     */     //   176: bipush #16
/*     */     //   178: ior
/*     */     //   179: istore_3
/*     */     //   180: goto -> 228
/*     */     //   183: aload #6
/*     */     //   185: checkcast com/mojang/brigadier/tree/LiteralCommandNode
/*     */     //   188: astore #10
/*     */     //   190: new net/minecraft/network/protocol/game/ClientboundCommandsPacket$LiteralNodeStub
/*     */     //   193: dup
/*     */     //   194: aload #10
/*     */     //   196: invokevirtual getLiteral : ()Ljava/lang/String;
/*     */     //   199: invokespecial <init> : (Ljava/lang/String;)V
/*     */     //   202: astore #5
/*     */     //   204: iload_3
/*     */     //   205: iconst_1
/*     */     //   206: ior
/*     */     //   207: istore_3
/*     */     //   208: goto -> 228
/*     */     //   211: new java/lang/UnsupportedOperationException
/*     */     //   214: dup
/*     */     //   215: aload_0
/*     */     //   216: invokestatic valueOf : (Ljava/lang/Object;)Ljava/lang/String;
/*     */     //   219: <illegal opcode> makeConcatWithConstants : (Ljava/lang/String;)Ljava/lang/String;
/*     */     //   224: invokespecial <init> : (Ljava/lang/String;)V
/*     */     //   227: athrow
/*     */     //   228: aload_0
/*     */     //   229: invokevirtual getChildren : ()Ljava/util/Collection;
/*     */     //   232: invokeinterface stream : ()Ljava/util/stream/Stream;
/*     */     //   237: aload_2
/*     */     //   238: dup
/*     */     //   239: invokestatic requireNonNull : (Ljava/lang/Object;)Ljava/lang/Object;
/*     */     //   242: pop
/*     */     //   243: <illegal opcode> applyAsInt : (Lit/unimi/dsi/fastutil/objects/Object2IntMap;)Ljava/util/function/ToIntFunction;
/*     */     //   248: invokeinterface mapToInt : (Ljava/util/function/ToIntFunction;)Ljava/util/stream/IntStream;
/*     */     //   253: invokeinterface toArray : ()[I
/*     */     //   258: astore #6
/*     */     //   260: new net/minecraft/network/protocol/game/ClientboundCommandsPacket$Entry
/*     */     //   263: dup
/*     */     //   264: aload #5
/*     */     //   266: iload_3
/*     */     //   267: iload #4
/*     */     //   269: aload #6
/*     */     //   271: invokespecial <init> : (Lnet/minecraft/network/protocol/game/ClientboundCommandsPacket$NodeStub;II[I)V
/*     */     //   274: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #204	-> 0
/*     */     //   #206	-> 2
/*     */     //   #207	-> 9
/*     */     //   #208	-> 14
/*     */     //   #210	-> 29
/*     */     //   #212	-> 32
/*     */     //   #213	-> 42
/*     */     //   #215	-> 46
/*     */     //   #216	-> 56
/*     */     //   #220	-> 61
/*     */     //   #221	-> 108
/*     */     //   #222	-> 115
/*     */     //   #223	-> 119
/*     */     //   #224	-> 122
/*     */     //   #225	-> 125
/*     */     //   #226	-> 132
/*     */     //   #227	-> 142
/*     */     //   #228	-> 166
/*     */     //   #229	-> 170
/*     */     //   #230	-> 175
/*     */     //   #232	-> 180
/*     */     //   #233	-> 183
/*     */     //   #234	-> 190
/*     */     //   #235	-> 204
/*     */     //   #236	-> 208
/*     */     //   #237	-> 211
/*     */     //   #240	-> 228
/*     */     //   #242	-> 260
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   26	3	4	redirect	I
/*     */     //   122	3	5	nodeStub	Lnet/minecraft/network/protocol/game/ClientboundCommandsPacket$NodeStub;
/*     */     //   115	10	8	ignored	Lcom/mojang/brigadier/tree/RootCommandNode;
/*     */     //   142	38	10	suggestionId	Lnet/minecraft/resources/Identifier;
/*     */     //   166	17	5	nodeStub	Lnet/minecraft/network/protocol/game/ClientboundCommandsPacket$NodeStub;
/*     */     //   132	51	9	arg	Lcom/mojang/brigadier/tree/ArgumentCommandNode;
/*     */     //   204	7	5	nodeStub	Lnet/minecraft/network/protocol/game/ClientboundCommandsPacket$NodeStub;
/*     */     //   190	21	10	literal	Lcom/mojang/brigadier/tree/LiteralCommandNode;
/*     */     //   0	275	0	node	Lcom/mojang/brigadier/tree/CommandNode;
/*     */     //   0	275	1	inspector	Lnet/minecraft/network/protocol/game/ClientboundCommandsPacket$NodeInspector;
/*     */     //   0	275	2	ids	Lit/unimi/dsi/fastutil/objects/Object2IntMap;
/*     */     //   2	273	3	flags	I
/*     */     //   32	243	4	redirect	I
/*     */     //   228	47	5	nodeStub	Lnet/minecraft/network/protocol/game/ClientboundCommandsPacket$NodeStub;
/*     */     //   260	15	6	childrenIds	[I
/*     */     // Local variable type table:
/*     */     //   start	length	slot	name	signature
/*     */     //   115	10	8	ignored	Lcom/mojang/brigadier/tree/RootCommandNode<TS;>;
/*     */     //   132	51	9	arg	Lcom/mojang/brigadier/tree/ArgumentCommandNode<TS;*>;
/*     */     //   190	21	10	literal	Lcom/mojang/brigadier/tree/LiteralCommandNode<TS;>;
/*     */     //   0	275	0	node	Lcom/mojang/brigadier/tree/CommandNode<TS;>;
/*     */     //   0	275	1	inspector	Lnet/minecraft/network/protocol/game/ClientboundCommandsPacket$NodeInspector<TS;>;
/*     */     //   0	275	2	ids	Lit/unimi/dsi/fastutil/objects/Object2IntMap<Lcom/mojang/brigadier/tree/CommandNode<TS;>;>; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 247 */   public PacketType<ClientboundCommandsPacket> type() { return GamePacketTypes.CLIENTBOUND_COMMANDS; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 252 */   public void handle(ClientGamePacketListener listener) { listener.handleCommands(this); }
/*     */ 
/*     */ 
/*     */   
/* 256 */   public <S> RootCommandNode<S> getRoot(CommandBuildContext context, NodeBuilder<S> builder) { return (RootCommandNode)(new NodeResolver(context, builder, this.entries)).resolve(this.rootIndex); }
/*     */   private static final class Entry extends Record { private final ClientboundCommandsPacket.NodeStub stub; private final int flags; private final int redirect; private final int[] children;
/*     */     
/* 259 */     private Entry(ClientboundCommandsPacket.NodeStub stub, int flags, int redirect, int[] children) { this.stub = stub; this.flags = flags; this.redirect = redirect; this.children = children; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/network/protocol/game/ClientboundCommandsPacket$Entry;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #259	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/network/protocol/game/ClientboundCommandsPacket$Entry; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/protocol/game/ClientboundCommandsPacket$Entry;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #259	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/network/protocol/game/ClientboundCommandsPacket$Entry; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/network/protocol/game/ClientboundCommandsPacket$Entry;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #259	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/network/protocol/game/ClientboundCommandsPacket$Entry;
/* 259 */       //   0	8	1	o	Ljava/lang/Object; } public ClientboundCommandsPacket.NodeStub stub() { return this.stub; } public int flags() { return this.flags; } public int redirect() { return this.redirect; } public int[] children() { return this.children; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void write(FriendlyByteBuf output) {
/* 266 */       output.writeByte(this.flags);
/* 267 */       output.writeVarIntArray(this.children);
/* 268 */       if ((this.flags & 0x8) != 0) {
/* 269 */         output.writeVarInt(this.redirect);
/*     */       }
/* 271 */       if (this.stub != null) {
/* 272 */         this.stub.write(output);
/*     */       }
/*     */     }
/*     */     
/*     */     public boolean canBuild(IntSet unbuiltNodes) {
/* 277 */       if ((this.flags & 0x8) != 0) {
/* 278 */         return !unbuiltNodes.contains(this.redirect);
/*     */       }
/* 280 */       return true;
/*     */     }
/*     */     
/*     */     public boolean canResolve(IntSet unresolvedNodes) {
/* 284 */       for (int child : this.children) {
/* 285 */         if (unresolvedNodes.contains(child)) {
/* 286 */           return false;
/*     */         }
/*     */       } 
/* 289 */       return true;
/*     */     } }
/*     */ 
/*     */   
/*     */   private static class NodeResolver<S> extends Object {
/*     */     private final CommandBuildContext context;
/*     */     private final ClientboundCommandsPacket.NodeBuilder<S> builder;
/*     */     private final List<ClientboundCommandsPacket.Entry> entries;
/*     */     private final List<CommandNode<S>> nodes;
/*     */     
/*     */     private NodeResolver(CommandBuildContext context, ClientboundCommandsPacket.NodeBuilder<S> builder, List<ClientboundCommandsPacket.Entry> entries) {
/* 300 */       this.context = context;
/* 301 */       this.builder = builder;
/* 302 */       this.entries = entries;
/* 303 */       ObjectArrayList<CommandNode<S>> nodes = new ObjectArrayList<CommandNode<S>>();
/* 304 */       nodes.size(entries.size());
/* 305 */       this.nodes = nodes;
/*     */     }
/*     */     
/*     */     public CommandNode<S> resolve(int index) {
/* 309 */       CommandNode<S> result, currentNode = (CommandNode)this.nodes.get(index);
/* 310 */       if (currentNode != null) {
/* 311 */         return currentNode;
/*     */       }
/*     */       
/* 314 */       ClientboundCommandsPacket.Entry entry = (ClientboundCommandsPacket.Entry)this.entries.get(index);
/*     */ 
/*     */       
/* 317 */       if (entry.stub == null) {
/* 318 */         result = new RootCommandNode();
/*     */       } else {
/* 320 */         ArgumentBuilder<S, ?> resultBuilder = entry.stub.build(this.context, this.builder);
/* 321 */         if ((entry.flags & 0x8) != 0) {
/* 322 */           resultBuilder.redirect(resolve(entry.redirect));
/*     */         }
/* 324 */         boolean isExecutable = ((entry.flags & 0x4) != 0);
/* 325 */         boolean isRestricted = ((entry.flags & 0x20) != 0);
/* 326 */         result = this.builder.configure(resultBuilder, isExecutable, isRestricted).build();
/*     */       } 
/* 328 */       this.nodes.set(index, result);
/*     */       
/* 330 */       for (int childId : entry.children) {
/* 331 */         CommandNode<S> child = resolve(childId);
/* 332 */         if (!(child instanceof RootCommandNode)) {
/* 333 */           result.addChild(child);
/*     */         }
/*     */       } 
/* 336 */       return result;
/*     */     }
/*     */   }
/*     */   
/*     */   public static interface NodeInspector<S> {
/*     */     Identifier suggestionId(ArgumentCommandNode<S, ?> param1ArgumentCommandNode);
/*     */     
/*     */     boolean isExecutable(CommandNode<S> param1CommandNode);
/*     */     
/*     */     boolean isRestricted(CommandNode<S> param1CommandNode);
/*     */   }
/*     */   
/*     */   private static interface NodeStub {
/*     */     <S> ArgumentBuilder<S, ?> build(CommandBuildContext param1CommandBuildContext, ClientboundCommandsPacket.NodeBuilder<S> param1NodeBuilder);
/*     */     
/*     */     void write(FriendlyByteBuf param1FriendlyByteBuf);
/*     */   }
/*     */   
/*     */   public static interface NodeBuilder<S> {
/*     */     ArgumentBuilder<S, ?> createLiteral(String param1String);
/*     */     
/*     */     ArgumentBuilder<S, ?> createArgument(String param1String, ArgumentType<?> param1ArgumentType, Identifier param1Identifier);
/*     */     
/*     */     ArgumentBuilder<S, ?> configure(ArgumentBuilder<S, ?> param1ArgumentBuilder, boolean param1Boolean1, boolean param1Boolean2);
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\protocol\game\ClientboundCommandsPacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */