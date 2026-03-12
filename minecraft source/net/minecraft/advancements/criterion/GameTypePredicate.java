/*    */ package net.minecraft.advancements.criterion;
/*    */ import java.util.List;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.world.level.GameType;
/*    */ 
/*    */ public final class GameTypePredicate extends Record {
/*    */   private final List<GameType> types;
/*    */   
/*  9 */   public GameTypePredicate(List<GameType> types) { this.types = types; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/advancements/criterion/GameTypePredicate;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*  9 */     //   0	7	0	this	Lnet/minecraft/advancements/criterion/GameTypePredicate; } public List<GameType> types() { return this.types; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/advancements/criterion/GameTypePredicate;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/advancements/criterion/GameTypePredicate; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/advancements/criterion/GameTypePredicate;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/advancements/criterion/GameTypePredicate;
/*    */     //   0	8	1	o	Ljava/lang/Object; }
/* 10 */   public static final GameTypePredicate ANY = of(GameType.values());
/* 11 */   public static final GameTypePredicate SURVIVAL_LIKE = of(new GameType[] { GameType.SURVIVAL, GameType.ADVENTURE });
/*    */   
/* 13 */   public static final Codec<GameTypePredicate> CODEC = GameType.CODEC.listOf().xmap(GameTypePredicate::new, GameTypePredicate::types);
/*    */ 
/*    */   
/* 16 */   public static GameTypePredicate of(GameType... types) { return new GameTypePredicate(Arrays.stream(types).toList()); }
/*    */ 
/*    */ 
/*    */   
/* 20 */   public boolean matches(GameType type) { return this.types.contains(type); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\advancements\criterion\GameTypePredicate.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */