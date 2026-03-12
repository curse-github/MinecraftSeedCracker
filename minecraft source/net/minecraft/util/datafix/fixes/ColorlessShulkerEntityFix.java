/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ 
/*    */ public class ColorlessShulkerEntityFix extends NamedEntityFix {
/*  9 */   public ColorlessShulkerEntityFix(Schema outputSchema, boolean changesType) { super(outputSchema, changesType, "Colorless shulker entity fix", References.ENTITY, "minecraft:shulker"); }
/*    */ 
/*    */ 
/*    */   
/*    */   protected Typed<?> fix(Typed<?> entity) {
/* 14 */     return entity.update(DSL.remainderFinder(), tag -> {
/* 15 */           if (tag.get("Color").asInt(0) == 10) {
/* 16 */             return tag.set("Color", tag.createByte((byte)16));
/*    */           }
/* 18 */           return tag;
/*    */         });
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\datafix\fixes\ColorlessShulkerEntityFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */