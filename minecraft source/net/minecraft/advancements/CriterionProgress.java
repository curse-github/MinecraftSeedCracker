/*    */ package net.minecraft.advancements;
/*    */ 
/*    */ import java.time.Instant;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class CriterionProgress
/*    */ {
/*    */   private Instant obtained;
/*    */   
/*    */   public CriterionProgress() {}
/*    */   
/* 15 */   public CriterionProgress(Instant obtained) { this.obtained = obtained; }
/*    */ 
/*    */ 
/*    */   
/* 19 */   public boolean isDone() { return (this.obtained != null); }
/*    */ 
/*    */ 
/*    */   
/* 23 */   public void grant() { this.obtained = Instant.now(); }
/*    */ 
/*    */ 
/*    */   
/* 27 */   public void revoke() { this.obtained = null; }
/*    */ 
/*    */ 
/*    */   
/* 31 */   public Instant getObtained() { return this.obtained; }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 37 */   public String toString() { return "CriterionProgress{obtained=" + String.valueOf((this.obtained == null) ? "false" : this.obtained) + "}"; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 42 */   public void serializeToNetwork(FriendlyByteBuf output) { output.writeNullable(this.obtained, FriendlyByteBuf::writeInstant); }
/*    */ 
/*    */   
/*    */   public static CriterionProgress fromNetwork(FriendlyByteBuf input) {
/* 46 */     CriterionProgress result = new CriterionProgress();
/* 47 */     result.obtained = (Instant)input.readNullable(FriendlyByteBuf::readInstant);
/* 48 */     return result;
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\advancements\CriterionProgress.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */