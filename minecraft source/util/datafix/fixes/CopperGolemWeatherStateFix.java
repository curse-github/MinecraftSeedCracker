/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ 
/*    */ public class CopperGolemWeatherStateFix
/*    */   extends NamedEntityFix {
/* 10 */   public CopperGolemWeatherStateFix(Schema outputSchema) { super(outputSchema, false, "CopperGolemWeatherStateFix", References.ENTITY, "minecraft:copper_golem"); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 15 */   protected Typed<?> fix(Typed<?> entity) { return entity.update(DSL.remainderFinder(), tag -> tag.update("weather_state", CopperGolemWeatherStateFix::fixWeatherState)); }
/*    */ 
/*    */   
/*    */   private static Dynamic<?> fixWeatherState(Dynamic<?> value) {
/* 19 */     switch (value.asInt(0)) { case 1: case 2: case 3:  }  return 
/*    */ 
/*    */ 
/*    */       
/* 23 */       value.createString("unaffected");
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\datafix\fixes\CopperGolemWeatherStateFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */