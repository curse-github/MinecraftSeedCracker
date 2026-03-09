/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import net.minecraft.util.datafix.schemas.NamespacedSchema;
/*    */ import org.apache.commons.lang3.mutable.MutableBoolean;
/*    */ 
/*    */ public class FixWolfHealth
/*    */   extends NamedEntityFix {
/*    */   private static final String WOLF_ID = "minecraft:wolf";
/*    */   private static final String WOLF_HEALTH = "minecraft:generic.max_health";
/*    */   
/* 15 */   public FixWolfHealth(Schema outputSchema) { super(outputSchema, false, "FixWolfHealth", References.ENTITY, "minecraft:wolf"); }
/*    */ 
/*    */ 
/*    */   
/*    */   protected Typed<?> fix(Typed<?> entity) {
/* 20 */     return entity.update(DSL.remainderFinder(), dynamic -> {
/* 21 */           MutableBoolean healthAdjusted = new MutableBoolean(false);
/* 22 */           dynamic = dynamic.update("Attributes", ());
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
/* 38 */           if (healthAdjusted.isTrue()) {
/* 39 */             dynamic = dynamic.update("Health", ());
/*    */           }
/* 41 */           return dynamic;
/*    */         });
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\datafix\fixes\FixWolfHealth.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */