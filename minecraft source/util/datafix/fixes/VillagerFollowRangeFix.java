/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class VillagerFollowRangeFix
/*    */   extends NamedEntityFix
/*    */ {
/*    */   private static final double ORIGINAL_VALUE = 16.0D;
/*    */   private static final double NEW_BASE_VALUE = 48.0D;
/*    */   
/* 17 */   public VillagerFollowRangeFix(Schema outputSchema) { super(outputSchema, false, "Villager Follow Range Fix", References.ENTITY, "minecraft:villager"); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 22 */   protected Typed<?> fix(Typed<?> entity) { return entity.update(DSL.remainderFinder(), VillagerFollowRangeFix::fixValue); }
/*    */ 
/*    */   
/*    */   private static Dynamic<?> fixValue(Dynamic<?> tag) {
/* 26 */     return tag.update("Attributes", attributes -> 
/* 27 */         tag.createList(attributes.asStream().map(())));
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\datafix\fixes\VillagerFollowRangeFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */