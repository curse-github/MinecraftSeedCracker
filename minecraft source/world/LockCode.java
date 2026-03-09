/*    */ package net.minecraft.world;
/*    */ import net.minecraft.advancements.criterion.ItemPredicate;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.storage.ValueOutput;
/*    */ 
/*    */ public final class LockCode extends Record {
/*    */   private final ItemPredicate predicate;
/*    */   
/* 10 */   public LockCode(ItemPredicate predicate) { this.predicate = predicate; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/LockCode;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 10 */     //   0	7	0	this	Lnet/minecraft/world/LockCode; } public ItemPredicate predicate() { return this.predicate; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/LockCode;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/LockCode; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/LockCode;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/LockCode;
/*    */     //   0	8	1	o	Ljava/lang/Object; }
/* 11 */   public static final LockCode NO_LOCK = new LockCode(ItemPredicate.Builder.item().build());
/*    */   
/* 13 */   public static final Codec<LockCode> CODEC = ItemPredicate.CODEC.xmap(LockCode::new, LockCode::predicate);
/*    */   
/*    */   public static final String TAG_LOCK = "lock";
/*    */ 
/*    */   
/* 18 */   public boolean unlocksWith(ItemStack itemStack) { return this.predicate.test(itemStack); }
/*    */ 
/*    */   
/*    */   public void addToTag(ValueOutput parent) {
/* 22 */     if (this != NO_LOCK) {
/* 23 */       parent.store("lock", CODEC, this);
/*    */     }
/*    */   }
/*    */ 
/*    */   
/* 28 */   public boolean canUnlock(Player player) { return (player.isSpectator() || unlocksWith(player.getMainHandItem())); }
/*    */ 
/*    */ 
/*    */   
/* 32 */   public static LockCode fromTag(ValueInput parent) { return (LockCode)parent.read("lock", CODEC).orElse(NO_LOCK); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\LockCode.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */