/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ 
/*    */ 
/*    */ public class TridentAnimationFix
/*    */   extends DataComponentRemainderFix
/*    */ {
/* 10 */   public TridentAnimationFix(Schema outputSchema) { super(outputSchema, "TridentAnimationFix", "minecraft:consumable"); }
/*    */ 
/*    */ 
/*    */   
/*    */   protected <T> Dynamic<T> fixComponent(Dynamic<T> input) {
/* 15 */     return input.update("animation", animation -> {
/* 16 */           String optional = (String)animation.asString().result().orElse("");
/* 17 */           if ("spear".equals(optional)) {
/* 18 */             return animation.createString("trident");
/*    */           }
/* 20 */           return animation;
/*    */         });
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\datafix\fixes\TridentAnimationFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */