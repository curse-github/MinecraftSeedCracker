/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import java.util.Optional;
/*    */ 
/*    */ public class LodestoneCompassComponentFix
/*    */   extends DataComponentRemainderFix
/*    */ {
/* 10 */   public LodestoneCompassComponentFix(Schema outputSchema) { super(outputSchema, "LodestoneCompassComponentFix", "minecraft:lodestone_target", "minecraft:lodestone_tracker"); }
/*    */ 
/*    */ 
/*    */   
/*    */   protected <T> Dynamic<T> fixComponent(Dynamic<T> input) {
/* 15 */     Optional<Dynamic<T>> pos = input.get("pos").result();
/* 16 */     Optional<Dynamic<T>> dimension = input.get("dimension").result();
/* 17 */     input = input.remove("pos").remove("dimension");
/* 18 */     if (pos.isPresent() && dimension.isPresent()) {
/* 19 */       input = input.set("target", input.emptyMap()
/* 20 */           .set("pos", (Dynamic)pos.get())
/* 21 */           .set("dimension", (Dynamic)dimension.get()));
/*    */     }
/*    */     
/* 24 */     return input;
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\datafix\fixes\LodestoneCompassComponentFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */