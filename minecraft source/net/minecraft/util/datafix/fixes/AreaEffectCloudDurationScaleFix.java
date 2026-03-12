/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ 
/*    */ public class AreaEffectCloudDurationScaleFix extends NamedEntityFix {
/*  9 */   public AreaEffectCloudDurationScaleFix(Schema outputSchema) { super(outputSchema, false, "AreaEffectCloudDurationScaleFix", References.ENTITY, "minecraft:area_effect_cloud"); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 14 */   protected Typed<?> fix(Typed<?> entity) { return entity.update(DSL.remainderFinder(), tag -> tag.set("potion_duration_scale", tag.createFloat(0.25F))); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\datafix\fixes\AreaEffectCloudDurationScaleFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */