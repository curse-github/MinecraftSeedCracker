/*    */ package net.minecraft.world.entity.ai.gossip;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import net.minecraft.util.StringRepresentable;
/*    */ 
/*    */ public static enum GossipType
/*    */   implements StringRepresentable {
/*  8 */   MAJOR_NEGATIVE("major_negative", -5, 100, 10, 10),
/*  9 */   MINOR_NEGATIVE("minor_negative", -1, 200, 20, 20),
/*    */   
/* 11 */   MINOR_POSITIVE("minor_positive", 1, 25, 1, 5),
/* 12 */   MAJOR_POSITIVE("major_positive", 5, 20, 0, 20),
/*    */   
/* 14 */   TRADING("trading", 1, 25, 2, 20);
/*    */   
/*    */   public static final int REPUTATION_CHANGE_PER_EVENT = 25;
/*    */   public static final int REPUTATION_CHANGE_PER_EVERLASTING_MEMORY = 20;
/*    */   public static final int REPUTATION_CHANGE_PER_TRADE = 2;
/*    */   public final String id;
/*    */   public final int weight;
/*    */   public final int max;
/*    */   public final int decayPerDay;
/*    */   public final int decayPerTransfer;
/*    */   public static final Codec<GossipType> CODEC;
/*    */   
/*    */   static  {
/* 27 */     CODEC = StringRepresentable.fromEnum(GossipType::values);
/*    */   }
/*    */   GossipType(String id, int weight, int max, int decayPerDay, int decayPerTransfer) {
/* 30 */     this.id = id;
/* 31 */     this.weight = weight;
/* 32 */     this.max = max;
/* 33 */     this.decayPerDay = decayPerDay;
/* 34 */     this.decayPerTransfer = decayPerTransfer;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 39 */   public String getSerializedName() { return this.id; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\ai\gossip\GossipType.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */