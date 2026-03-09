/*     */ package net.minecraft.util.datafix.fixes;
/*     */ 
/*     */ import com.mojang.datafixers.DSL;
/*     */ import com.mojang.datafixers.DataFix;
/*     */ import com.mojang.datafixers.DataFixUtils;
/*     */ import com.mojang.datafixers.TypeRewriteRule;
/*     */ import com.mojang.datafixers.Typed;
/*     */ import com.mojang.datafixers.schemas.Schema;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import com.mojang.serialization.Dynamic;
/*     */ import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
/*     */ import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
/*     */ import java.util.Map;
/*     */ import java.util.stream.Collectors;
/*     */ 
/*     */ public class OptionsKeyLwjgl3Fix extends DataFix {
/*  17 */   public OptionsKeyLwjgl3Fix(Schema outputSchema, boolean changesType) { super(outputSchema, changesType); }
/*     */ 
/*     */   
/*     */   public static final String KEY_UNKNOWN = "key.unknown";
/*  21 */   private static final Int2ObjectMap<String> MAP = (Int2ObjectMap)DataFixUtils.make(new Int2ObjectOpenHashMap(), map -> {
/*  22 */         map.put(0, "key.unknown");
/*     */         
/*  24 */         map.put(11, "key.0");
/*  25 */         map.put(2, "key.1");
/*  26 */         map.put(3, "key.2");
/*  27 */         map.put(4, "key.3");
/*  28 */         map.put(5, "key.4");
/*  29 */         map.put(6, "key.5");
/*  30 */         map.put(7, "key.6");
/*  31 */         map.put(8, "key.7");
/*  32 */         map.put(9, "key.8");
/*  33 */         map.put(10, "key.9");
/*  34 */         map.put(30, "key.a");
/*  35 */         map.put(40, "key.apostrophe");
/*  36 */         map.put(48, "key.b");
/*  37 */         map.put(43, "key.backslash");
/*  38 */         map.put(14, "key.backspace");
/*  39 */         map.put(46, "key.c");
/*  40 */         map.put(58, "key.caps.lock");
/*  41 */         map.put(51, "key.comma");
/*  42 */         map.put(32, "key.d");
/*  43 */         map.put(211, "key.delete");
/*  44 */         map.put(208, "key.down");
/*  45 */         map.put(18, "key.e");
/*  46 */         map.put(207, "key.end");
/*  47 */         map.put(28, "key.enter");
/*  48 */         map.put(13, "key.equal");
/*  49 */         map.put(1, "key.escape");
/*  50 */         map.put(33, "key.f");
/*  51 */         map.put(59, "key.f1");
/*  52 */         map.put(68, "key.f10");
/*  53 */         map.put(87, "key.f11");
/*  54 */         map.put(88, "key.f12");
/*  55 */         map.put(100, "key.f13");
/*  56 */         map.put(101, "key.f14");
/*  57 */         map.put(102, "key.f15");
/*  58 */         map.put(103, "key.f16");
/*  59 */         map.put(104, "key.f17");
/*  60 */         map.put(105, "key.f18");
/*  61 */         map.put(113, "key.f19");
/*  62 */         map.put(60, "key.f2");
/*  63 */         map.put(61, "key.f3");
/*  64 */         map.put(62, "key.f4");
/*  65 */         map.put(63, "key.f5");
/*  66 */         map.put(64, "key.f6");
/*  67 */         map.put(65, "key.f7");
/*  68 */         map.put(66, "key.f8");
/*  69 */         map.put(67, "key.f9");
/*  70 */         map.put(34, "key.g");
/*  71 */         map.put(41, "key.grave.accent");
/*  72 */         map.put(35, "key.h");
/*  73 */         map.put(199, "key.home");
/*  74 */         map.put(23, "key.i");
/*  75 */         map.put(210, "key.insert");
/*  76 */         map.put(36, "key.j");
/*  77 */         map.put(37, "key.k");
/*  78 */         map.put(82, "key.keypad.0");
/*  79 */         map.put(79, "key.keypad.1");
/*  80 */         map.put(80, "key.keypad.2");
/*  81 */         map.put(81, "key.keypad.3");
/*  82 */         map.put(75, "key.keypad.4");
/*  83 */         map.put(76, "key.keypad.5");
/*  84 */         map.put(77, "key.keypad.6");
/*  85 */         map.put(71, "key.keypad.7");
/*  86 */         map.put(72, "key.keypad.8");
/*  87 */         map.put(73, "key.keypad.9");
/*  88 */         map.put(78, "key.keypad.add");
/*  89 */         map.put(83, "key.keypad.decimal");
/*  90 */         map.put(181, "key.keypad.divide");
/*  91 */         map.put(156, "key.keypad.enter");
/*  92 */         map.put(141, "key.keypad.equal");
/*  93 */         map.put(55, "key.keypad.multiply");
/*  94 */         map.put(74, "key.keypad.subtract");
/*  95 */         map.put(38, "key.l");
/*  96 */         map.put(203, "key.left");
/*  97 */         map.put(56, "key.left.alt");
/*  98 */         map.put(26, "key.left.bracket");
/*  99 */         map.put(29, "key.left.control");
/* 100 */         map.put(42, "key.left.shift");
/* 101 */         map.put(219, "key.left.win");
/* 102 */         map.put(50, "key.m");
/* 103 */         map.put(12, "key.minus");
/* 104 */         map.put(49, "key.n");
/* 105 */         map.put(69, "key.num.lock");
/* 106 */         map.put(24, "key.o");
/* 107 */         map.put(25, "key.p");
/* 108 */         map.put(209, "key.page.down");
/* 109 */         map.put(201, "key.page.up");
/* 110 */         map.put(197, "key.pause");
/* 111 */         map.put(52, "key.period");
/* 112 */         map.put(183, "key.print.screen");
/* 113 */         map.put(16, "key.q");
/* 114 */         map.put(19, "key.r");
/* 115 */         map.put(205, "key.right");
/* 116 */         map.put(184, "key.right.alt");
/* 117 */         map.put(27, "key.right.bracket");
/* 118 */         map.put(157, "key.right.control");
/* 119 */         map.put(54, "key.right.shift");
/* 120 */         map.put(220, "key.right.win");
/* 121 */         map.put(31, "key.s");
/* 122 */         map.put(70, "key.scroll.lock");
/* 123 */         map.put(39, "key.semicolon");
/* 124 */         map.put(53, "key.slash");
/* 125 */         map.put(57, "key.space");
/* 126 */         map.put(20, "key.t");
/* 127 */         map.put(15, "key.tab");
/* 128 */         map.put(22, "key.u");
/* 129 */         map.put(200, "key.up");
/* 130 */         map.put(47, "key.v");
/* 131 */         map.put(17, "key.w");
/* 132 */         map.put(45, "key.x");
/* 133 */         map.put(21, "key.y");
/* 134 */         map.put(44, "key.z");
/*     */       });
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
/*     */   public TypeRewriteRule makeRule() {
/* 159 */     return fixTypeEverywhereTyped("OptionsKeyLwjgl3Fix", getInputSchema().getType(References.OPTIONS), input -> input.update(DSL.remainderFinder(), ()));
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\datafix\fixes\OptionsKeyLwjgl3Fix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */