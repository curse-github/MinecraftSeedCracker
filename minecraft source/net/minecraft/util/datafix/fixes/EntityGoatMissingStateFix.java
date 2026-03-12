/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ 
/*    */ public class EntityGoatMissingStateFix
/*    */   extends NamedEntityFix {
/* 10 */   public EntityGoatMissingStateFix(Schema outputSchema) { super(outputSchema, false, "EntityGoatMissingStateFix", References.ENTITY, "minecraft:goat"); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 15 */   protected Typed<?> fix(Typed<?> entity) { return entity.update(DSL.remainderFinder(), tag -> tag.set("HasLeftHorn", tag.createBoolean(true)).set("HasRightHorn", tag.createBoolean(true))); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\datafix\fixes\EntityGoatMissingStateFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */