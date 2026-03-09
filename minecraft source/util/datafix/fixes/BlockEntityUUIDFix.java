/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ 
/*    */ public class BlockEntityUUIDFix extends AbstractUUIDFix {
/*  9 */   public BlockEntityUUIDFix(Schema outputSchema) { super(outputSchema, References.BLOCK_ENTITY); }
/*    */ 
/*    */ 
/*    */   
/*    */   protected TypeRewriteRule makeRule() {
/* 14 */     return fixTypeEverywhereTyped("BlockEntityUUIDFix", getInputSchema().getType(this.typeReference), input -> {
/* 15 */           input = updateNamedChoice(input, "minecraft:conduit", this::updateConduit);
/* 16 */           return updateNamedChoice(input, "minecraft:skull", this::updateSkull);
/*    */         });
/*    */   }
/*    */ 
/*    */   
/*    */   private Dynamic<?> updateSkull(Dynamic<?> tag) {
/* 22 */     return (Dynamic)tag.get("Owner").get().map(ownerTag -> 
/* 23 */         (Dynamic)replaceUUIDString(ownerTag, "Id", "Id").orElse(ownerTag))
/* 24 */       .map(ownerTag -> 
/* 25 */         tag.remove("Owner").set("SkullOwner", ownerTag))
/* 26 */       .result().orElse(tag);
/*    */   }
/*    */ 
/*    */   
/* 30 */   private Dynamic<?> updateConduit(Dynamic<?> tag) { return (Dynamic)replaceUUIDMLTag(tag, "target_uuid", "Target").orElse(tag); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\datafix\fixes\BlockEntityUUIDFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */