/*    */ package net.minecraft.world.level.gamerules;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.chat.MutableComponent;
/*    */ import net.minecraft.resources.Identifier;
/*    */ 
/*    */ public final class GameRuleCategory extends Record {
/*    */   private final Identifier id;
/*    */   
/* 11 */   public GameRuleCategory(Identifier id) { this.id = id; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/gamerules/GameRuleCategory;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 11 */     //   0	7	0	this	Lnet/minecraft/world/level/gamerules/GameRuleCategory; } public Identifier id() { return this.id; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/gamerules/GameRuleCategory;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/gamerules/GameRuleCategory; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/gamerules/GameRuleCategory;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/level/gamerules/GameRuleCategory;
/*    */     //   0	8	1	o	Ljava/lang/Object; }
/* 12 */   private static final List<GameRuleCategory> SORT_ORDER = new ArrayList();
/* 13 */   public static final GameRuleCategory PLAYER = register("player");
/* 14 */   public static final GameRuleCategory MOBS = register("mobs");
/* 15 */   public static final GameRuleCategory SPAWNING = register("spawning");
/* 16 */   public static final GameRuleCategory DROPS = register("drops");
/* 17 */   public static final GameRuleCategory UPDATES = register("updates");
/* 18 */   public static final GameRuleCategory CHAT = register("chat");
/* 19 */   public static final GameRuleCategory MISC = register("misc");
/*    */ 
/*    */   
/* 22 */   public Identifier getDescriptionId() { return this.id; }
/*    */ 
/*    */ 
/*    */   
/* 26 */   private static GameRuleCategory register(String name) { return register(Identifier.withDefaultNamespace(name)); }
/*    */ 
/*    */   
/*    */   public static GameRuleCategory register(Identifier id) {
/* 30 */     GameRuleCategory category = new GameRuleCategory(id);
/* 31 */     if (SORT_ORDER.contains(category)) {
/* 32 */       throw new IllegalArgumentException(String.format(Locale.ROOT, "Category '%s' is already registered.", new Object[] { id }));
/*    */     }
/* 34 */     SORT_ORDER.add(category);
/* 35 */     return category;
/*    */   }
/*    */ 
/*    */   
/* 39 */   public MutableComponent label() { return Component.translatable(this.id.toLanguageKey("gamerule.category")); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\gamerules\GameRuleCategory.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */