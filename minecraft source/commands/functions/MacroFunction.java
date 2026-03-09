/*     */ package net.minecraft.commands.functions;
/*     */ 
/*     */ import com.mojang.brigadier.CommandDispatcher;
/*     */ import com.mojang.brigadier.StringReader;
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import it.unimi.dsi.fastutil.ints.IntList;
/*     */ import it.unimi.dsi.fastutil.ints.IntLists;
/*     */ import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
/*     */ import java.text.DecimalFormat;
/*     */ import java.text.DecimalFormatSymbols;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import net.minecraft.commands.ExecutionCommandSource;
/*     */ import net.minecraft.commands.FunctionInstantiationException;
/*     */ import net.minecraft.commands.execution.UnboundEntryAction;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.nbt.Tag;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.util.Util;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MacroFunction<T extends ExecutionCommandSource<T>>
/*     */   extends Object
/*     */   implements CommandFunction<T>
/*     */ {
/*  33 */   private static final DecimalFormat DECIMAL_FORMAT = (DecimalFormat)Util.make(new DecimalFormat("#", DecimalFormatSymbols.getInstance(Locale.ROOT)), format -> 
/*  34 */       format.setMaximumFractionDigits(15));
/*     */   
/*     */   private static final int MAX_CACHE_ENTRIES = 8;
/*     */   private final List<String> parameters;
/*     */   
/*     */   public MacroFunction(Identifier id, List<Entry<T>> entries, List<String> parameters) {
/*  40 */     this.cache = new Object2ObjectLinkedOpenHashMap(8, 0.25F);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  46 */     this.id = id;
/*  47 */     this.entries = entries;
/*  48 */     this.parameters = parameters;
/*     */   }
/*     */   private final Object2ObjectLinkedOpenHashMap<List<String>, InstantiatedFunction<T>> cache; private final Identifier id;
/*     */   private final List<Entry<T>> entries;
/*     */   
/*  53 */   public Identifier id() { return this.id; }
/*     */ 
/*     */ 
/*     */   
/*     */   public InstantiatedFunction<T> instantiate(CompoundTag arguments, CommandDispatcher<T> dispatcher) throws FunctionInstantiationException {
/*  58 */     if (arguments == null) {
/*  59 */       throw new FunctionInstantiationException(Component.translatable("commands.function.error.missing_arguments", new Object[] { Component.translationArg(id()) }));
/*     */     }
/*  61 */     List<String> parameterValues = new ArrayList<String>(this.parameters.size());
/*  62 */     for (String argument : this.parameters) {
/*  63 */       Tag argumentValue = arguments.get(argument);
/*  64 */       if (argumentValue == null) {
/*  65 */         throw new FunctionInstantiationException(Component.translatable("commands.function.error.missing_argument", new Object[] { Component.translationArg(id()), argument }));
/*     */       }
/*  67 */       parameterValues.add(stringify(argumentValue));
/*     */     } 
/*     */     
/*  70 */     InstantiatedFunction<T> cachedFunction = (InstantiatedFunction)this.cache.getAndMoveToLast(parameterValues);
/*  71 */     if (cachedFunction != null) {
/*  72 */       return cachedFunction;
/*     */     }
/*  74 */     if (this.cache.size() >= 8) {
/*  75 */       this.cache.removeFirst();
/*     */     }
/*  77 */     InstantiatedFunction<T> function = substituteAndParse(this.parameters, parameterValues, dispatcher);
/*  78 */     this.cache.put(parameterValues, function);
/*  79 */     return function;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static String stringify(Tag tag) { // Byte code:
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
/*     */     //   16: tableswitch default -> 208, 0 -> 56, 1 -> 83, 2 -> 111, 3 -> 136, 4 -> 161, 5 -> 186
/*     */     //   56: aload_1
/*     */     //   57: checkcast net/minecraft/nbt/FloatTag
/*     */     //   60: astore_3
/*     */     //   61: aload_3
/*     */     //   62: invokevirtual value : ()F
/*     */     //   65: fstore #5
/*     */     //   67: fload #5
/*     */     //   69: fstore #4
/*     */     //   71: getstatic net/minecraft/commands/functions/MacroFunction.DECIMAL_FORMAT : Ljava/text/DecimalFormat;
/*     */     //   74: fload #4
/*     */     //   76: f2d
/*     */     //   77: invokevirtual format : (D)Ljava/lang/String;
/*     */     //   80: goto -> 214
/*     */     //   83: aload_1
/*     */     //   84: checkcast net/minecraft/nbt/DoubleTag
/*     */     //   87: astore #5
/*     */     //   89: aload #5
/*     */     //   91: invokevirtual value : ()D
/*     */     //   94: dstore #8
/*     */     //   96: dload #8
/*     */     //   98: dstore #6
/*     */     //   100: getstatic net/minecraft/commands/functions/MacroFunction.DECIMAL_FORMAT : Ljava/text/DecimalFormat;
/*     */     //   103: dload #6
/*     */     //   105: invokevirtual format : (D)Ljava/lang/String;
/*     */     //   108: goto -> 214
/*     */     //   111: aload_1
/*     */     //   112: checkcast net/minecraft/nbt/ByteTag
/*     */     //   115: astore #8
/*     */     //   117: aload #8
/*     */     //   119: invokevirtual value : ()B
/*     */     //   122: istore #10
/*     */     //   124: iload #10
/*     */     //   126: istore #9
/*     */     //   128: iload #9
/*     */     //   130: invokestatic valueOf : (I)Ljava/lang/String;
/*     */     //   133: goto -> 214
/*     */     //   136: aload_1
/*     */     //   137: checkcast net/minecraft/nbt/ShortTag
/*     */     //   140: astore #10
/*     */     //   142: aload #10
/*     */     //   144: invokevirtual value : ()S
/*     */     //   147: istore #12
/*     */     //   149: iload #12
/*     */     //   151: istore #11
/*     */     //   153: iload #11
/*     */     //   155: invokestatic valueOf : (I)Ljava/lang/String;
/*     */     //   158: goto -> 214
/*     */     //   161: aload_1
/*     */     //   162: checkcast net/minecraft/nbt/LongTag
/*     */     //   165: astore #12
/*     */     //   167: aload #12
/*     */     //   169: invokevirtual value : ()J
/*     */     //   172: lstore #15
/*     */     //   174: lload #15
/*     */     //   176: lstore #13
/*     */     //   178: lload #13
/*     */     //   180: invokestatic valueOf : (J)Ljava/lang/String;
/*     */     //   183: goto -> 214
/*     */     //   186: aload_1
/*     */     //   187: checkcast net/minecraft/nbt/StringTag
/*     */     //   190: astore #15
/*     */     //   192: aload #15
/*     */     //   194: invokevirtual value : ()Ljava/lang/String;
/*     */     //   197: astore #17
/*     */     //   199: aload #17
/*     */     //   201: astore #16
/*     */     //   203: aload #16
/*     */     //   205: goto -> 214
/*     */     //   208: aload_0
/*     */     //   209: invokeinterface toString : ()Ljava/lang/String;
/*     */     //   214: areturn
/*     */     //   215: astore_1
/*     */     //   216: new java/lang/MatchException
/*     */     //   219: dup
/*     */     //   220: aload_1
/*     */     //   221: invokevirtual toString : ()Ljava/lang/String;
/*     */     //   224: aload_1
/*     */     //   225: invokespecial <init> : (Ljava/lang/String;Ljava/lang/Throwable;)V
/*     */     //   228: athrow
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #83	-> 0
/*     */     //   #85	-> 56
/*     */     //   #86	-> 83
/*     */     //   #87	-> 111
/*     */     //   #88	-> 136
/*     */     //   #89	-> 161
/*     */     //   #91	-> 186
/*     */     //   #92	-> 208
/*     */     //   #83	-> 214
/*     */     //   #91	-> 215
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   71	12	4	value	F
/*     */     //   100	11	6	value	D
/*     */     //   128	8	9	value	B
/*     */     //   153	8	11	value	S
/*     */     //   178	8	13	value	J
/*     */     //   203	5	16	value	Ljava/lang/String;
/*     */     //   0	229	0	tag	Lnet/minecraft/nbt/Tag;
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   62	65	215	java/lang/Throwable
/*     */     //   91	94	215	java/lang/Throwable
/*     */     //   119	122	215	java/lang/Throwable
/*     */     //   144	147	215	java/lang/Throwable
/*     */     //   169	172	215	java/lang/Throwable
/*     */     //   194	197	215	java/lang/Throwable }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void lookupValues(List<String> values, IntList indicesToSelect, List<String> selectedValuesOutput) {
/*  97 */     selectedValuesOutput.clear();
/*  98 */     indicesToSelect.forEach(index -> selectedValuesOutput.add((String)values.get(index)));
/*     */   }
/*     */   
/*     */   private InstantiatedFunction<T> substituteAndParse(List<String> keys, List<String> values, CommandDispatcher<T> dispatcher) throws FunctionInstantiationException {
/* 102 */     List<UnboundEntryAction<T>> newEntries = new ArrayList<UnboundEntryAction<T>>(this.entries.size());
/* 103 */     List<String> entryArguments = new ArrayList<String>(values.size());
/*     */     
/* 105 */     for (Entry<T> entry : this.entries) {
/* 106 */       lookupValues(values, entry.parameters(), entryArguments);
/* 107 */       newEntries.add(entry.instantiate(entryArguments, dispatcher, this.id));
/*     */     } 
/* 109 */     return new PlainTextFunction(id().withPath(id -> id + "/" + id), newEntries);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static class PlainTextEntry<T>
/*     */     extends Object
/*     */     implements Entry<T>
/*     */   {
/*     */     private final UnboundEntryAction<T> compiledAction;
/*     */ 
/*     */ 
/*     */     
/* 122 */     public PlainTextEntry(UnboundEntryAction<T> compiledAction) { this.compiledAction = compiledAction; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 127 */     public IntList parameters() { return IntLists.emptyList(); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 132 */     public UnboundEntryAction<T> instantiate(List<String> substitutions, CommandDispatcher<T> dispatcher, Identifier functionId) throws FunctionInstantiationException { return this.compiledAction; }
/*     */   }
/*     */   
/*     */   static class MacroEntry<T extends ExecutionCommandSource<T>>
/*     */     extends Object implements Entry<T> {
/*     */     private final StringTemplate template;
/*     */     private final IntList parameters;
/*     */     private final T compilationContext;
/*     */     
/*     */     public MacroEntry(StringTemplate template, IntList parameters, T compilationContext) {
/* 142 */       this.template = template;
/* 143 */       this.parameters = parameters;
/* 144 */       this.compilationContext = compilationContext;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 149 */     public IntList parameters() { return this.parameters; }
/*     */ 
/*     */ 
/*     */     
/*     */     public UnboundEntryAction<T> instantiate(List<String> substitutions, CommandDispatcher<T> dispatcher, Identifier functionId) throws FunctionInstantiationException {
/* 154 */       String command = this.template.substitute(substitutions);
/*     */       try {
/* 156 */         return CommandFunction.parseCommand(dispatcher, this.compilationContext, new StringReader(command));
/* 157 */       } catch (CommandSyntaxException e) {
/* 158 */         throw new FunctionInstantiationException(Component.translatable("commands.function.error.parse", new Object[] { Component.translationArg(functionId), command, e.getMessage() }));
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   static interface Entry<T> {
/*     */     IntList parameters();
/*     */     
/*     */     UnboundEntryAction<T> instantiate(List<String> param1List, CommandDispatcher<T> param1CommandDispatcher, Identifier param1Identifier) throws FunctionInstantiationException;
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\commands\functions\MacroFunction.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */