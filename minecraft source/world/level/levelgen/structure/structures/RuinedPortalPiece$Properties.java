/*    */ package net.minecraft.world.level.levelgen.structure.structures;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.util.Function6;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
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
/*    */ public class Properties
/*    */ {
/* 60 */   public static final Codec<Properties> CODEC = RecordCodecBuilder.create(i -> i.group(Codec.BOOL
/* 61 */         .fieldOf("cold").forGetter(()), Codec.FLOAT
/* 62 */         .fieldOf("mossiness").forGetter(()), Codec.BOOL
/* 63 */         .fieldOf("air_pocket").forGetter(()), Codec.BOOL
/* 64 */         .fieldOf("overgrown").forGetter(()), Codec.BOOL
/* 65 */         .fieldOf("vines").forGetter(()), Codec.BOOL
/* 66 */         .fieldOf("replace_with_blackstone").forGetter(()))
/* 67 */       .apply(i, Properties::new));
/*    */   
/*    */   public boolean cold;
/*    */   
/*    */   public float mossiness;
/*    */   public boolean airPocket;
/*    */   public boolean overgrown;
/*    */   public boolean vines;
/*    */   public boolean replaceWithBlackstone;
/*    */   
/*    */   public Properties() {}
/*    */   
/*    */   public Properties(boolean cold, float mossiness, boolean airPocket, boolean overgrown, boolean vines, boolean replaceWithBlackstone) {
/* 80 */     this.cold = cold;
/* 81 */     this.mossiness = mossiness;
/* 82 */     this.airPocket = airPocket;
/* 83 */     this.overgrown = overgrown;
/* 84 */     this.vines = vines;
/* 85 */     this.replaceWithBlackstone = replaceWithBlackstone;
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\structure\structures\RuinedPortalPiece$Properties.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */