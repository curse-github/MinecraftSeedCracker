/*    */ package net.minecraft.world.entity.raid;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import net.minecraft.util.StringRepresentable;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ static enum RaidStatus
/*    */   implements StringRepresentable
/*    */ {
/* 69 */   ONGOING("ongoing"),
/* 70 */   VICTORY("victory"),
/* 71 */   LOSS("loss"),
/* 72 */   STOPPED("stopped");
/*    */   static  {
/* 74 */     CODEC = StringRepresentable.fromEnum(RaidStatus::values);
/*    */   }
/*    */   public static final Codec<RaidStatus> CODEC;
/*    */   private final String name;
/*    */   
/* 79 */   RaidStatus(String name) { this.name = name; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 84 */   public String getSerializedName() { return this.name; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\raid\Raid$RaidStatus.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */