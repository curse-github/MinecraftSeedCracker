/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ 
/*    */ public class BlockEntityShulkerBoxColorFix extends NamedEntityFix {
/*  9 */   public BlockEntityShulkerBoxColorFix(Schema outputSchema, boolean changesType) { super(outputSchema, changesType, "BlockEntityShulkerBoxColorFix", References.BLOCK_ENTITY, "minecraft:shulker_box"); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 14 */   protected Typed<?> fix(Typed<?> entity) { return entity.update(DSL.remainderFinder(), tag -> tag.remove("Color")); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\datafix\fixes\BlockEntityShulkerBoxColorFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */