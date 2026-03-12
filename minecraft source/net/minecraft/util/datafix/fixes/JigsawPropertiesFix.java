/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ 
/*    */ public class JigsawPropertiesFix
/*    */   extends NamedEntityFix {
/* 10 */   public JigsawPropertiesFix(Schema schema, boolean changesType) { super(schema, changesType, "JigsawPropertiesFix", References.BLOCK_ENTITY, "minecraft:jigsaw"); }
/*    */ 
/*    */   
/*    */   private static Dynamic<?> fixTag(Dynamic<?> tag) {
/* 14 */     String oldName = tag.get("attachement_type").asString("minecraft:empty");
/* 15 */     String oldPool = tag.get("target_pool").asString("minecraft:empty");
/* 16 */     return tag
/* 17 */       .set("name", tag.createString(oldName))
/* 18 */       .set("target", tag.createString(oldName))
/* 19 */       .remove("attachement_type")
/* 20 */       .set("pool", tag.createString(oldPool))
/* 21 */       .remove("target_pool");
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 26 */   protected Typed<?> fix(Typed<?> entity) { return entity.update(DSL.remainderFinder(), JigsawPropertiesFix::fixTag); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\datafix\fixes\JigsawPropertiesFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */