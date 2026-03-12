/*    */ package net.minecraft.util.datafix.fixes;
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFixUtils;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import java.util.Objects;
/*    */ import java.util.stream.Stream;
/*    */ 
/*    */ public class BlockEntityBannerColorFix extends NamedEntityFix {
/* 11 */   public BlockEntityBannerColorFix(Schema outputSchema, boolean changesType) { super(outputSchema, changesType, "BlockEntityBannerColorFix", References.BLOCK_ENTITY, "minecraft:banner"); }
/*    */ 
/*    */   
/*    */   public Dynamic<?> fixTag(Dynamic<?> input) {
/* 15 */     input = input.update("Base", base -> base.createInt(15 - base.asInt(0)));
/*    */     
/* 17 */     return input.update("Patterns", list -> {
/*    */ 
/*    */           
/* 20 */           Objects.requireNonNull(list); return (Dynamic)DataFixUtils.orElse(list.asStreamOpt().map(()).map(list::createList).result(), list);
/*    */         });
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 28 */   protected Typed<?> fix(Typed<?> entity) { return entity.update(DSL.remainderFinder(), this::fixTag); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\datafix\fixes\BlockEntityBannerColorFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */