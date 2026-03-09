/*     */ package net.minecraft.server.commands.data;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.collect.Iterables;
/*     */ import com.mojang.brigadier.CommandDispatcher;
/*     */ import com.mojang.brigadier.Message;
/*     */ import com.mojang.brigadier.arguments.DoubleArgumentType;
/*     */ import com.mojang.brigadier.arguments.IntegerArgumentType;
/*     */ import com.mojang.brigadier.builder.ArgumentBuilder;
/*     */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*     */ import com.mojang.brigadier.builder.RequiredArgumentBuilder;
/*     */ import com.mojang.brigadier.context.CommandContext;
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import com.mojang.brigadier.exceptions.Dynamic2CommandExceptionType;
/*     */ import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
/*     */ import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.function.BiConsumer;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.Supplier;
/*     */ import net.minecraft.commands.CommandSourceStack;
/*     */ import net.minecraft.commands.Commands;
/*     */ import net.minecraft.commands.arguments.CompoundTagArgument;
/*     */ import net.minecraft.commands.arguments.NbtPathArgument;
/*     */ import net.minecraft.commands.arguments.NbtTagArgument;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.nbt.NumericTag;
/*     */ import net.minecraft.nbt.StringTag;
/*     */ import net.minecraft.nbt.Tag;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.util.Mth;
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
/*     */ public class DataCommands
/*     */ {
/*  48 */   private static final SimpleCommandExceptionType ERROR_MERGE_UNCHANGED = new SimpleCommandExceptionType(Component.translatable("commands.data.merge.failed"));
/*  49 */   private static final DynamicCommandExceptionType ERROR_GET_NOT_NUMBER = new DynamicCommandExceptionType(path -> Component.translatableEscape("commands.data.get.invalid", new Object[] { path }));
/*  50 */   private static final DynamicCommandExceptionType ERROR_GET_NON_EXISTENT = new DynamicCommandExceptionType(path -> Component.translatableEscape("commands.data.get.unknown", new Object[] { path }));
/*  51 */   private static final SimpleCommandExceptionType ERROR_MULTIPLE_TAGS = new SimpleCommandExceptionType(Component.translatable("commands.data.get.multiple"));
/*  52 */   private static final DynamicCommandExceptionType ERROR_EXPECTED_OBJECT = new DynamicCommandExceptionType(node -> Component.translatableEscape("commands.data.modify.expected_object", new Object[] { node }));
/*  53 */   private static final DynamicCommandExceptionType ERROR_EXPECTED_VALUE = new DynamicCommandExceptionType(node -> Component.translatableEscape("commands.data.modify.expected_value", new Object[] { node }));
/*  54 */   private static final Dynamic2CommandExceptionType ERROR_INVALID_SUBSTRING = new Dynamic2CommandExceptionType((start, end) -> Component.translatableEscape("commands.data.modify.invalid_substring", new Object[] { start, end }));
/*     */   
/*  56 */   public static final List<Function<String, DataProvider>> ALL_PROVIDERS = ImmutableList.of(EntityDataAccessor.PROVIDER, BlockDataAccessor.PROVIDER, StorageDataAccessor.PROVIDER);
/*     */   
/*  58 */   public static final List<DataProvider> TARGET_PROVIDERS = (List)ALL_PROVIDERS.stream().map(f -> (DataProvider)f.apply("target")).collect(ImmutableList.toImmutableList());
/*  59 */   public static final List<DataProvider> SOURCE_PROVIDERS = (List)ALL_PROVIDERS.stream().map(f -> (DataProvider)f.apply("source")).collect(ImmutableList.toImmutableList());
/*     */   
/*     */   public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
/*  62 */     LiteralArgumentBuilder<CommandSourceStack> root = (LiteralArgumentBuilder)Commands.literal("data").requires(Commands.hasPermission(Commands.LEVEL_GAMEMASTERS));
/*     */     
/*  64 */     for (DataProvider targetProvider : TARGET_PROVIDERS) {
/*  65 */       ((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)root
/*  66 */         .then(targetProvider
/*  67 */           .wrap(Commands.literal("merge"), p -> p
/*  68 */             .then(
/*  69 */               Commands.argument("nbt", CompoundTagArgument.compoundTag())
/*  70 */               .executes(())))))
/*     */ 
/*     */ 
/*     */         
/*  74 */         .then(targetProvider
/*  75 */           .wrap(Commands.literal("get"), p -> p
/*  76 */             .executes(())
/*  77 */             .then((
/*  78 */               (RequiredArgumentBuilder)Commands.argument("path", NbtPathArgument.nbtPath())
/*  79 */               .executes(()))
/*  80 */               .then(
/*  81 */                 Commands.argument("scale", DoubleArgumentType.doubleArg())
/*  82 */                 .executes(()))))))
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*  87 */         .then(targetProvider
/*  88 */           .wrap(Commands.literal("remove"), p -> p
/*  89 */             .then(
/*  90 */               Commands.argument("path", NbtPathArgument.nbtPath())
/*  91 */               .executes(())))))
/*     */ 
/*     */ 
/*     */         
/*  95 */         .then(
/*  96 */           decorateModification((parent, rest) -> 
/*  97 */             parent
/*  98 */             .then(
/*  99 */               Commands.literal("insert")
/* 100 */               .then(
/* 101 */                 Commands.argument("index", IntegerArgumentType.integer())
/* 102 */                 .then(rest
/* 103 */                   .create(()))))
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */             
/* 109 */             .then(
/* 110 */               Commands.literal("prepend")
/* 111 */               .then(rest
/* 112 */                 .create(())))
/*     */ 
/*     */             
/* 115 */             .then(
/* 116 */               Commands.literal("append")
/* 117 */               .then(rest
/* 118 */                 .create(())))
/*     */ 
/*     */             
/* 121 */             .then(
/* 122 */               Commands.literal("set")
/* 123 */               .then(rest
/* 124 */                 .create(())))
/*     */ 
/*     */             
/* 127 */             .then(
/* 128 */               Commands.literal("merge")
/* 129 */               .then(rest
/* 130 */                 .create(())))));
/*     */     }
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
/* 166 */     dispatcher.register(root);
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
/*     */   private static String getAsText(Tag tag) throws CommandSyntaxException { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: dup
/*     */     //   2: invokestatic requireNonNull : (Ljava/lang/Object;)Ljava/lang/Object;
/*     */     //   5: pop
/*     */     //   6: astore_1
/*     */     //   7: iconst_0
/*     */     //   8: istore_2
/*     */     //   9: aload_1
/*     */     //   10: iload_2
/*     */     //   11: <illegal opcode> typeSwitch : (Ljava/lang/Object;I)I
/*     */     //   16: lookupswitch default -> 80, 0 -> 44, 1 -> 64
/*     */     //   44: aload_1
/*     */     //   45: checkcast net/minecraft/nbt/StringTag
/*     */     //   48: astore_3
/*     */     //   49: aload_3
/*     */     //   50: invokevirtual value : ()Ljava/lang/String;
/*     */     //   53: astore #5
/*     */     //   55: aload #5
/*     */     //   57: astore #4
/*     */     //   59: aload #4
/*     */     //   61: goto -> 88
/*     */     //   64: aload_1
/*     */     //   65: checkcast net/minecraft/nbt/PrimitiveTag
/*     */     //   68: astore #5
/*     */     //   70: aload #5
/*     */     //   72: invokeinterface toString : ()Ljava/lang/String;
/*     */     //   77: goto -> 88
/*     */     //   80: getstatic net/minecraft/server/commands/data/DataCommands.ERROR_EXPECTED_VALUE : Lcom/mojang/brigadier/exceptions/DynamicCommandExceptionType;
/*     */     //   83: aload_0
/*     */     //   84: invokevirtual create : (Ljava/lang/Object;)Lcom/mojang/brigadier/exceptions/CommandSyntaxException;
/*     */     //   87: athrow
/*     */     //   88: areturn
/*     */     //   89: astore_1
/*     */     //   90: new java/lang/MatchException
/*     */     //   93: dup
/*     */     //   94: aload_1
/*     */     //   95: invokevirtual toString : ()Ljava/lang/String;
/*     */     //   98: aload_1
/*     */     //   99: invokespecial <init> : (Ljava/lang/String;Ljava/lang/Throwable;)V
/*     */     //   102: athrow
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #180	-> 0
/*     */     //   #181	-> 44
/*     */     //   #182	-> 64
/*     */     //   #183	-> 80
/*     */     //   #180	-> 88
/*     */     //   #182	-> 89
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   59	5	4	value	Ljava/lang/String;
/*     */     //   70	10	5	primitiveTag	Lnet/minecraft/nbt/PrimitiveTag;
/*     */     //   0	103	0	tag	Lnet/minecraft/nbt/Tag;
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   50	53	89	java/lang/Throwable }
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
/*     */   private static List<Tag> stringifyTagList(List<Tag> source, StringProcessor stringProcessor) throws CommandSyntaxException {
/* 193 */     List<Tag> result = new ArrayList<Tag>(source.size());
/* 194 */     for (Tag tag : source) {
/* 195 */       String text = getAsText(tag);
/* 196 */       result.add(StringTag.valueOf(stringProcessor.process(text)));
/*     */     } 
/* 198 */     return result;
/*     */   }
/*     */   
/*     */   private static ArgumentBuilder<CommandSourceStack, ?> decorateModification(BiConsumer<ArgumentBuilder<CommandSourceStack, ?>, DataManipulatorDecorator> nodeSupplier) {
/* 202 */     LiteralArgumentBuilder<CommandSourceStack> modify = Commands.literal("modify");
/*     */     
/* 204 */     for (Iterator iterator = TARGET_PROVIDERS.iterator(); iterator.hasNext(); ) { DataProvider targetProvider = (DataProvider)iterator.next();
/* 205 */       targetProvider.wrap(modify, t -> {
/* 206 */             RequiredArgumentBuilder requiredArgumentBuilder = Commands.argument("targetPath", NbtPathArgument.nbtPath());
/*     */             
/* 208 */             for (DataProvider sourceProvider : SOURCE_PROVIDERS) {
/* 209 */               nodeSupplier.accept(requiredArgumentBuilder, ());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */               
/* 217 */               nodeSupplier.accept(requiredArgumentBuilder, ());
/*     */             } 
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
/* 233 */             nodeSupplier.accept(requiredArgumentBuilder, ());
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
/* 244 */             return t.then(requiredArgumentBuilder);
/*     */           }); }
/*     */ 
/*     */ 
/*     */     
/* 249 */     return modify;
/*     */   }
/*     */   
/*     */   private static String validatedSubstring(String input, int start, int end) throws CommandSyntaxException {
/* 253 */     if (start < 0 || end > input.length() || start > end) {
/* 254 */       throw ERROR_INVALID_SUBSTRING.create(Integer.valueOf(start), Integer.valueOf(end));
/*     */     }
/* 256 */     return input.substring(start, end);
/*     */   }
/*     */   
/*     */   private static String substring(String input, int start, int end) throws CommandSyntaxException {
/* 260 */     int length = input.length();
/* 261 */     int absoluteStart = getOffset(start, length);
/* 262 */     int absoluteEnd = getOffset(end, length);
/* 263 */     return validatedSubstring(input, absoluteStart, absoluteEnd);
/*     */   }
/*     */   
/*     */   private static String substring(String input, int start) throws CommandSyntaxException {
/* 267 */     int length = input.length();
/* 268 */     return validatedSubstring(input, getOffset(start, length), length);
/*     */   }
/*     */ 
/*     */   
/* 272 */   private static int getOffset(int index, int length) { return (index >= 0) ? index : (length + index); }
/*     */ 
/*     */   
/*     */   private static List<Tag> getSingletonSource(CommandContext<CommandSourceStack> context, DataProvider sourceProvider) throws CommandSyntaxException {
/* 276 */     DataAccessor source = sourceProvider.access(context);
/* 277 */     return Collections.singletonList(source.getData());
/*     */   }
/*     */   
/*     */   private static List<Tag> resolveSourcePath(CommandContext<CommandSourceStack> context, DataProvider sourceProvider) throws CommandSyntaxException {
/* 281 */     DataAccessor source = sourceProvider.access(context);
/* 282 */     NbtPathArgument.NbtPath sourcePath = NbtPathArgument.getPath(context, "sourcePath");
/* 283 */     return sourcePath.get(source.getData());
/*     */   }
/*     */   
/*     */   private static int manipulateData(CommandContext<CommandSourceStack> context, DataProvider targetProvider, DataManipulator manipulator, List<Tag> source) throws CommandSyntaxException {
/* 287 */     DataAccessor target = targetProvider.access(context);
/* 288 */     NbtPathArgument.NbtPath targetPath = NbtPathArgument.getPath(context, "targetPath");
/*     */     
/* 290 */     CompoundTag targetData = target.getData();
/*     */     
/* 292 */     int result = manipulator.modify(context, targetData, targetPath, source);
/*     */     
/* 294 */     if (result == 0) {
/* 295 */       throw ERROR_MERGE_UNCHANGED.create();
/*     */     }
/*     */     
/* 298 */     target.setData(targetData);
/* 299 */     ((CommandSourceStack)context.getSource()).sendSuccess(() -> target.getModifiedSuccess(), true);
/*     */     
/* 301 */     return result;
/*     */   }
/*     */   
/*     */   private static int removeData(CommandSourceStack source, DataAccessor accessor, NbtPathArgument.NbtPath path) throws CommandSyntaxException {
/* 305 */     CompoundTag result = accessor.getData();
/*     */     
/* 307 */     int count = path.remove(result);
/*     */     
/* 309 */     if (count == 0) {
/* 310 */       throw ERROR_MERGE_UNCHANGED.create();
/*     */     }
/*     */     
/* 313 */     accessor.setData(result);
/* 314 */     source.sendSuccess(() -> accessor.getModifiedSuccess(), true);
/* 315 */     return count;
/*     */   }
/*     */   
/*     */   public static Tag getSingleTag(NbtPathArgument.NbtPath path, DataAccessor accessor) throws CommandSyntaxException {
/* 319 */     Collection<Tag> tags = path.get(accessor.getData());
/* 320 */     Iterator<Tag> iterator = tags.iterator();
/* 321 */     Tag result = (Tag)iterator.next();
/* 322 */     if (iterator.hasNext()) {
/* 323 */       throw ERROR_MULTIPLE_TAGS.create();
/*     */     }
/*     */     
/* 326 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static int getData(CommandSourceStack source, DataAccessor accessor, NbtPathArgument.NbtPath path) throws CommandSyntaxException { // Byte code:
/*     */     //   0: aload_2
/*     */     //   1: aload_1
/*     */     //   2: invokestatic getSingleTag : (Lnet/minecraft/commands/arguments/NbtPathArgument$NbtPath;Lnet/minecraft/server/commands/data/DataAccessor;)Lnet/minecraft/nbt/Tag;
/*     */     //   5: astore_3
/*     */     //   6: aload_3
/*     */     //   7: dup
/*     */     //   8: invokestatic requireNonNull : (Ljava/lang/Object;)Ljava/lang/Object;
/*     */     //   11: pop
/*     */     //   12: astore #5
/*     */     //   14: iconst_0
/*     */     //   15: istore #6
/*     */     //   17: aload #5
/*     */     //   19: iload #6
/*     */     //   21: <illegal opcode> typeSwitch : (Ljava/lang/Object;I)I
/*     */     //   26: tableswitch default -> 60, 0 -> 70, 1 -> 90, 2 -> 107, 3 -> 122, 4 -> 148
/*     */     //   60: new java/lang/MatchException
/*     */     //   63: dup
/*     */     //   64: aconst_null
/*     */     //   65: aconst_null
/*     */     //   66: invokespecial <init> : (Ljava/lang/String;Ljava/lang/Throwable;)V
/*     */     //   69: athrow
/*     */     //   70: aload #5
/*     */     //   72: checkcast net/minecraft/nbt/NumericTag
/*     */     //   75: astore #7
/*     */     //   77: aload #7
/*     */     //   79: invokeinterface doubleValue : ()D
/*     */     //   84: invokestatic floor : (D)I
/*     */     //   87: goto -> 166
/*     */     //   90: aload #5
/*     */     //   92: checkcast net/minecraft/nbt/CollectionTag
/*     */     //   95: astore #8
/*     */     //   97: aload #8
/*     */     //   99: invokeinterface size : ()I
/*     */     //   104: goto -> 166
/*     */     //   107: aload #5
/*     */     //   109: checkcast net/minecraft/nbt/CompoundTag
/*     */     //   112: astore #9
/*     */     //   114: aload #9
/*     */     //   116: invokevirtual size : ()I
/*     */     //   119: goto -> 166
/*     */     //   122: aload #5
/*     */     //   124: checkcast net/minecraft/nbt/StringTag
/*     */     //   127: astore #10
/*     */     //   129: aload #10
/*     */     //   131: invokevirtual value : ()Ljava/lang/String;
/*     */     //   134: astore #12
/*     */     //   136: aload #12
/*     */     //   138: astore #11
/*     */     //   140: aload #11
/*     */     //   142: invokevirtual length : ()I
/*     */     //   145: goto -> 166
/*     */     //   148: aload #5
/*     */     //   150: checkcast net/minecraft/nbt/EndTag
/*     */     //   153: astore #12
/*     */     //   155: getstatic net/minecraft/server/commands/data/DataCommands.ERROR_GET_NON_EXISTENT : Lcom/mojang/brigadier/exceptions/DynamicCommandExceptionType;
/*     */     //   158: aload_2
/*     */     //   159: invokevirtual toString : ()Ljava/lang/String;
/*     */     //   162: invokevirtual create : (Ljava/lang/Object;)Lcom/mojang/brigadier/exceptions/CommandSyntaxException;
/*     */     //   165: athrow
/*     */     //   166: istore #4
/*     */     //   168: aload_0
/*     */     //   169: aload_1
/*     */     //   170: aload_3
/*     */     //   171: <illegal opcode> get : (Lnet/minecraft/server/commands/data/DataAccessor;Lnet/minecraft/nbt/Tag;)Ljava/util/function/Supplier;
/*     */     //   176: iconst_0
/*     */     //   177: invokevirtual sendSuccess : (Ljava/util/function/Supplier;Z)V
/*     */     //   180: iload #4
/*     */     //   182: ireturn
/*     */     //   183: astore #5
/*     */     //   185: new java/lang/MatchException
/*     */     //   188: dup
/*     */     //   189: aload #5
/*     */     //   191: invokevirtual toString : ()Ljava/lang/String;
/*     */     //   194: aload #5
/*     */     //   196: invokespecial <init> : (Ljava/lang/String;Ljava/lang/Throwable;)V
/*     */     //   199: athrow
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #330	-> 0
/*     */     //   #331	-> 6
/*     */     //   #332	-> 70
/*     */     //   #333	-> 90
/*     */     //   #334	-> 107
/*     */     //   #335	-> 122
/*     */     //   #336	-> 148
/*     */     //   #338	-> 168
/*     */     //   #339	-> 180
/*     */     //   #336	-> 183
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   77	13	7	numericTag	Lnet/minecraft/nbt/NumericTag;
/*     */     //   97	10	8	collectionTag	Lnet/minecraft/nbt/CollectionTag;
/*     */     //   114	8	9	compoundTag	Lnet/minecraft/nbt/CompoundTag;
/*     */     //   140	8	11	value	Ljava/lang/String;
/*     */     //   155	11	12	ignored	Lnet/minecraft/nbt/EndTag;
/*     */     //   6	177	3	tag	Lnet/minecraft/nbt/Tag;
/*     */     //   168	15	4	result	I
/*     */     //   0	200	0	source	Lnet/minecraft/commands/CommandSourceStack;
/*     */     //   0	200	1	accessor	Lnet/minecraft/server/commands/data/DataAccessor;
/*     */     //   0	200	2	path	Lnet/minecraft/commands/arguments/NbtPathArgument$NbtPath;
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   131	134	183	java/lang/Throwable }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static int getNumeric(CommandSourceStack source, DataAccessor accessor, NbtPathArgument.NbtPath path, double scale) throws CommandSyntaxException {
/* 343 */     Tag tag = getSingleTag(path, accessor);
/* 344 */     if (!(tag instanceof NumericTag)) {
/* 345 */       throw ERROR_GET_NOT_NUMBER.create(path.toString());
/*     */     }
/* 347 */     int result = Mth.floor(((NumericTag)tag).doubleValue() * scale);
/* 348 */     source.sendSuccess(() -> accessor.getPrintSuccess(path, scale, result), false);
/* 349 */     return result;
/*     */   }
/*     */   
/*     */   private static int getData(CommandSourceStack source, DataAccessor accessor) throws CommandSyntaxException {
/* 353 */     CompoundTag data = accessor.getData();
/* 354 */     source.sendSuccess(() -> accessor.getPrintSuccess(data), false);
/* 355 */     return 1;
/*     */   }
/*     */   
/*     */   private static int mergeData(CommandSourceStack source, DataAccessor accessor, CompoundTag nbt) throws CommandSyntaxException {
/* 359 */     CompoundTag old = accessor.getData();
/*     */     
/* 361 */     if (NbtPathArgument.NbtPath.isTooDeep(nbt, 0)) {
/* 362 */       throw NbtPathArgument.ERROR_DATA_TOO_DEEP.create();
/*     */     }
/*     */     
/* 365 */     CompoundTag result = old.copy().merge(nbt);
/* 366 */     if (old.equals(result)) {
/* 367 */       throw ERROR_MERGE_UNCHANGED.create();
/*     */     }
/*     */     
/* 370 */     accessor.setData(result);
/*     */     
/* 372 */     source.sendSuccess(() -> accessor.getModifiedSuccess(), true);
/* 373 */     return 1;
/*     */   }
/*     */   
/*     */   public static interface DataProvider {
/*     */     DataAccessor access(CommandContext<CommandSourceStack> param1CommandContext) throws CommandSyntaxException;
/*     */     
/*     */     ArgumentBuilder<CommandSourceStack, ?> wrap(ArgumentBuilder<CommandSourceStack, ?> param1ArgumentBuilder, Function<ArgumentBuilder<CommandSourceStack, ?>, ArgumentBuilder<CommandSourceStack, ?>> param1Function);
/*     */   }
/*     */   
/*     */   @FunctionalInterface
/*     */   private static interface StringProcessor {
/*     */     String process(String param1String) throws CommandSyntaxException;
/*     */   }
/*     */   
/*     */   @FunctionalInterface
/*     */   private static interface DataManipulator {
/*     */     int modify(CommandContext<CommandSourceStack> param1CommandContext, CompoundTag param1CompoundTag, NbtPathArgument.NbtPath param1NbtPath, List<Tag> param1List) throws CommandSyntaxException;
/*     */   }
/*     */   
/*     */   @FunctionalInterface
/*     */   private static interface DataManipulatorDecorator {
/*     */     ArgumentBuilder<CommandSourceStack, ?> create(DataCommands.DataManipulator param1DataManipulator);
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\commands\data\DataCommands.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */