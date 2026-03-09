/*    */ package net.minecraft.tags;
/*    */ 
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.world.entity.ai.village.poi.PoiType;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class PoiTypeTags
/*    */ {
/* 11 */   public static final TagKey<PoiType> ACQUIRABLE_JOB_SITE = create("acquirable_job_site");
/* 12 */   public static final TagKey<PoiType> VILLAGE = create("village");
/* 13 */   public static final TagKey<PoiType> BEE_HOME = create("bee_home");
/*    */ 
/*    */   
/* 16 */   private static TagKey<PoiType> create(String name) { return TagKey.create(Registries.POINT_OF_INTEREST_TYPE, Identifier.withDefaultNamespace(name)); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\tags\PoiTypeTags.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */