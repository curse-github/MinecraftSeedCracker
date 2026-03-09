/*    */ package net.minecraft.data.worldgen;
/*    */ 
/*    */ import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
/*    */ 
/*    */ public class VillagePools {
/*    */   public static void bootstrap(BootstrapContext<StructureTemplatePool> context) {
/*  7 */     PlainVillagePools.bootstrap(context);
/*  8 */     SnowyVillagePools.bootstrap(context);
/*  9 */     SavannaVillagePools.bootstrap(context);
/* 10 */     DesertVillagePools.bootstrap(context);
/* 11 */     TaigaVillagePools.bootstrap(context);
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\data\worldgen\VillagePools.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */