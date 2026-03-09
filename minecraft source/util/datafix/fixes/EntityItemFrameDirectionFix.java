/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ 
/*    */ public class EntityItemFrameDirectionFix
/*    */   extends NamedEntityFix {
/* 10 */   public EntityItemFrameDirectionFix(Schema outputSchema, boolean changesType) { super(outputSchema, changesType, "EntityItemFrameDirectionFix", References.ENTITY, "minecraft:item_frame"); }
/*    */ 
/*    */ 
/*    */   
/* 14 */   public Dynamic<?> fixTag(Dynamic<?> input) { return input.set("Facing", input.createByte(direction2dTo3d(input.get("Facing").asByte((byte)0)))); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 19 */   protected Typed<?> fix(Typed<?> entity) { return entity.update(DSL.remainderFinder(), this::fixTag); }
/*    */ 
/*    */   
/*    */   private static byte direction2dTo3d(byte dir) {
/* 23 */     switch (dir)
/*    */     
/*    */     { default:
/* 26 */         return 2;
/*    */       case 0:
/* 28 */         return 3;
/*    */       case 1:
/* 30 */         return 4;
/*    */       case 3:
/* 32 */         break; }  return 5;
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\datafix\fixes\EntityItemFrameDirectionFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */