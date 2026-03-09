/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ 
/*    */ public class EntityShulkerColorFix
/*    */   extends NamedEntityFix {
/* 10 */   public EntityShulkerColorFix(Schema outputSchema, boolean changesType) { super(outputSchema, changesType, "EntityShulkerColorFix", References.ENTITY, "minecraft:shulker"); }
/*    */ 
/*    */   
/*    */   public Dynamic<?> fixTag(Dynamic<?> input) {
/* 14 */     if (input.get("Color").map(Dynamic::asNumber).result().isEmpty()) {
/* 15 */       return input.set("Color", input.createByte((byte)10));
/*    */     }
/* 17 */     return input;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 22 */   protected Typed<?> fix(Typed<?> entity) { return entity.update(DSL.remainderFinder(), this::fixTag); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\datafix\fixes\EntityShulkerColorFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */