/*     */ package net.minecraft.network.chat;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.Optional;
/*     */ import java.util.function.BiFunction;
/*     */ import net.minecraft.commands.CommandSourceStack;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.core.RegistryAccess;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.data.worldgen.BootstrapContext;
/*     */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ 
/*     */ public final class ChatType extends Record {
/*     */   private final ChatTypeDecoration chat;
/*     */   private final ChatTypeDecoration narration;
/*     */   
/*  20 */   public ChatType(ChatTypeDecoration chat, ChatTypeDecoration narration) { this.chat = chat; this.narration = narration; } public final String toString() { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/chat/ChatType;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #20	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*  20 */     //   0	7	0	this	Lnet/minecraft/network/chat/ChatType; } public ChatTypeDecoration chat() { return this.chat; } public final int hashCode() { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/chat/ChatType;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #20	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/network/chat/ChatType; } public final boolean equals(Object o) { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/chat/ChatType;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #20	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/network/chat/ChatType;
/*  20 */     //   0	8	1	o	Ljava/lang/Object; } public ChatTypeDecoration narration() { return this.narration; }
/*  21 */   public static final Codec<ChatType> DIRECT_CODEC = RecordCodecBuilder.create(i -> i.group(ChatTypeDecoration.CODEC
/*  22 */         .fieldOf("chat").forGetter(ChatType::chat), ChatTypeDecoration.CODEC
/*  23 */         .fieldOf("narration").forGetter(ChatType::narration))
/*  24 */       .apply(i, ChatType::new));
/*     */   
/*  26 */   public static final StreamCodec<RegistryFriendlyByteBuf, ChatType> DIRECT_STREAM_CODEC = StreamCodec.composite(ChatTypeDecoration.STREAM_CODEC, ChatType::chat, ChatTypeDecoration.STREAM_CODEC, ChatType::narration, ChatType::new);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  32 */   public static final StreamCodec<RegistryFriendlyByteBuf, Holder<ChatType>> STREAM_CODEC = ByteBufCodecs.holder(Registries.CHAT_TYPE, DIRECT_STREAM_CODEC);
/*     */   
/*  34 */   public static final ChatTypeDecoration DEFAULT_CHAT_DECORATION = ChatTypeDecoration.withSender("chat.type.text");
/*     */   
/*  36 */   public static final ResourceKey<ChatType> CHAT = create("chat");
/*     */   
/*  38 */   public static final ResourceKey<ChatType> SAY_COMMAND = create("say_command");
/*  39 */   public static final ResourceKey<ChatType> MSG_COMMAND_INCOMING = create("msg_command_incoming");
/*  40 */   public static final ResourceKey<ChatType> MSG_COMMAND_OUTGOING = create("msg_command_outgoing");
/*  41 */   public static final ResourceKey<ChatType> TEAM_MSG_COMMAND_INCOMING = create("team_msg_command_incoming");
/*  42 */   public static final ResourceKey<ChatType> TEAM_MSG_COMMAND_OUTGOING = create("team_msg_command_outgoing");
/*  43 */   public static final ResourceKey<ChatType> EMOTE_COMMAND = create("emote_command");
/*     */ 
/*     */   
/*  46 */   private static ResourceKey<ChatType> create(String name) { return ResourceKey.create(Registries.CHAT_TYPE, Identifier.withDefaultNamespace(name)); }
/*     */ 
/*     */   
/*     */   public static void bootstrap(BootstrapContext<ChatType> context) {
/*  50 */     context.register(CHAT, new ChatType(DEFAULT_CHAT_DECORATION, 
/*     */           
/*  52 */           ChatTypeDecoration.withSender("chat.type.text.narrate")));
/*     */ 
/*     */     
/*  55 */     context.register(SAY_COMMAND, new ChatType(
/*  56 */           ChatTypeDecoration.withSender("chat.type.announcement"), 
/*  57 */           ChatTypeDecoration.withSender("chat.type.text.narrate")));
/*     */     
/*  59 */     context.register(MSG_COMMAND_INCOMING, new ChatType(
/*  60 */           ChatTypeDecoration.incomingDirectMessage("commands.message.display.incoming"), 
/*  61 */           ChatTypeDecoration.withSender("chat.type.text.narrate")));
/*     */     
/*  63 */     context.register(MSG_COMMAND_OUTGOING, new ChatType(
/*  64 */           ChatTypeDecoration.outgoingDirectMessage("commands.message.display.outgoing"), 
/*  65 */           ChatTypeDecoration.withSender("chat.type.text.narrate")));
/*     */     
/*  67 */     context.register(TEAM_MSG_COMMAND_INCOMING, new ChatType(
/*  68 */           ChatTypeDecoration.teamMessage("chat.type.team.text"), 
/*  69 */           ChatTypeDecoration.withSender("chat.type.text.narrate")));
/*     */     
/*  71 */     context.register(TEAM_MSG_COMMAND_OUTGOING, new ChatType(
/*  72 */           ChatTypeDecoration.teamMessage("chat.type.team.sent"), 
/*  73 */           ChatTypeDecoration.withSender("chat.type.text.narrate")));
/*     */     
/*  75 */     context.register(EMOTE_COMMAND, new ChatType(
/*  76 */           ChatTypeDecoration.withSender("chat.type.emote"), 
/*  77 */           ChatTypeDecoration.withSender("chat.type.emote")));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  82 */   public static Bound bind(ResourceKey<ChatType> chatType, Entity entity) { return bind(chatType, entity.level().registryAccess(), entity.getDisplayName()); }
/*     */ 
/*     */ 
/*     */   
/*  86 */   public static Bound bind(ResourceKey<ChatType> chatType, CommandSourceStack source) { return bind(chatType, source.registryAccess(), source.getDisplayName()); }
/*     */ 
/*     */   
/*     */   public static Bound bind(ResourceKey<ChatType> chatType, RegistryAccess registryAccess, Component name) {
/*  90 */     Registry<ChatType> registry = registryAccess.lookupOrThrow(Registries.CHAT_TYPE);
/*  91 */     return new Bound(registry.getOrThrow(chatType), name);
/*     */   }
/*     */   public static final class Bound extends Record { private final Holder<ChatType> chatType; private final Component name; private final Optional<Component> targetName;
/*  94 */     public Bound(Holder<ChatType> chatType, Component name, Optional<Component> targetName) { this.chatType = chatType; this.name = name; this.targetName = targetName; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/network/chat/ChatType$Bound;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #94	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/network/chat/ChatType$Bound; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/chat/ChatType$Bound;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #94	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/network/chat/ChatType$Bound; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/network/chat/ChatType$Bound;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #94	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/network/chat/ChatType$Bound;
/*  94 */       //   0	8	1	o	Ljava/lang/Object; } public Holder<ChatType> chatType() { return this.chatType; } public Component name() { return this.name; } public Optional<Component> targetName() { return this.targetName; }
/*  95 */     public static final StreamCodec<RegistryFriendlyByteBuf, Bound> STREAM_CODEC = StreamCodec.composite(ChatType.STREAM_CODEC, Bound::chatType, ComponentSerialization.TRUSTED_STREAM_CODEC, Bound::name, ComponentSerialization.TRUSTED_OPTIONAL_STREAM_CODEC, Bound::targetName, Bound::new);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 103 */     private Bound(Holder<ChatType> chatType, Component name) { this(chatType, name, Optional.empty()); }
/*     */ 
/*     */ 
/*     */     
/* 107 */     public Component decorate(Component content) { return ((ChatType)this.chatType.value()).chat().decorate(content, this); }
/*     */ 
/*     */ 
/*     */     
/* 111 */     public Component decorateNarration(Component content) { return ((ChatType)this.chatType.value()).narration().decorate(content, this); }
/*     */ 
/*     */ 
/*     */     
/* 115 */     public Bound withTargetName(Component targetName) { return new Bound(this.chatType, this.name, Optional.of(targetName)); } }
/*     */ 
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\chat\ChatType.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */