/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import java.util.Map;
/*    */ import java.util.Optional;
/*    */ 
/*    */ 
/*    */ public class PrimedTntBlockStateFixer
/*    */   extends NamedEntityWriteReadFix
/*    */ {
/* 12 */   public PrimedTntBlockStateFixer(Schema outputSchema) { super(outputSchema, true, "PrimedTnt BlockState fixer", References.ENTITY, "minecraft:tnt"); }
/*    */ 
/*    */   
/*    */   private static <T> Dynamic<T> renameFuse(Dynamic<T> input) {
/* 16 */     Optional<Dynamic<T>> fuseValue = input.get("Fuse").get().result();
/* 17 */     if (fuseValue.isPresent()) {
/* 18 */       return input.set("fuse", (Dynamic)fuseValue.get());
/*    */     }
/* 20 */     return input;
/*    */   }
/*    */   
/*    */   private static <T> Dynamic<T> insertBlockState(Dynamic<T> input) {
/* 24 */     return input.set("block_state", input.createMap(Map.of(input
/* 25 */             .createString("Name"), input.createString("minecraft:tnt"))));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 31 */   protected <T> Dynamic<T> fix(Dynamic<T> input) { return renameFuse(insertBlockState(input)); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\datafix\fixes\PrimedTntBlockStateFixer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */