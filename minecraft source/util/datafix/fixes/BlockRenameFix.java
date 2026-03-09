/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.DataFixUtils;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.Type;
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import com.mojang.serialization.DynamicOps;
/*    */ import java.util.Objects;
/*    */ import java.util.Optional;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.util.datafix.schemas.NamespacedSchema;
/*    */ 
/*    */ public abstract class BlockRenameFix extends DataFix {
/*    */   private final String name;
/*    */   
/*    */   public BlockRenameFix(Schema outputSchema, String name) {
/* 22 */     super(outputSchema, false);
/* 23 */     this.name = name;
/*    */   }
/*    */ 
/*    */   
/*    */   public TypeRewriteRule makeRule() {
/* 28 */     Type<?> blockType = getInputSchema().getType(References.BLOCK_NAME);
/* 29 */     Type<Pair<String, String>> expectedType = DSL.named(References.BLOCK_NAME.typeName(), NamespacedSchema.namespacedString());
/* 30 */     if (!Objects.equals(blockType, expectedType)) {
/* 31 */       throw new IllegalStateException("block type is not what was expected.");
/*    */     }
/*    */     
/* 34 */     TypeRewriteRule blockRule = fixTypeEverywhere(this.name + " for block", expectedType, ops -> ());
/*    */     
/* 36 */     TypeRewriteRule blockStateRule = fixTypeEverywhereTyped(this.name + " for block_state", getInputSchema().getType(References.BLOCK_STATE), input -> input.update(DSL.remainderFinder(), this::fixBlockState));
/*    */     
/* 38 */     TypeRewriteRule flatBlockStateRule = fixTypeEverywhereTyped(this.name + " for flat_block_state", getInputSchema().getType(References.FLAT_BLOCK_STATE), input -> input.update(DSL.remainderFinder(), ()));
/*    */ 
/*    */ 
/*    */     
/* 42 */     return TypeRewriteRule.seq(blockRule, new TypeRewriteRule[] { blockStateRule, flatBlockStateRule });
/*    */   }
/*    */   
/*    */   private Dynamic<?> fixBlockState(Dynamic<?> tag) {
/* 46 */     Optional<String> name = tag.get("Name").asString().result();
/* 47 */     if (name.isPresent()) {
/* 48 */       return tag.set("Name", tag.createString(renameBlock((String)name.get())));
/*    */     }
/* 50 */     return tag;
/*    */   }
/*    */   
/*    */   private String fixFlatBlockState(String string) {
/* 54 */     int startProperties = string.indexOf('[');
/* 55 */     int startNbt = string.indexOf('{');
/* 56 */     int end = string.length();
/* 57 */     if (startProperties > 0) {
/* 58 */       end = startProperties;
/*    */     }
/* 60 */     if (startNbt > 0) {
/* 61 */       end = Math.min(end, startNbt);
/*    */     }
/*    */     
/* 64 */     String name = string.substring(0, end);
/* 65 */     String newName = renameBlock(name);
/* 66 */     return newName + newName;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public static DataFix create(Schema outputSchema, String name, final Function<String, String> renamer) {
/* 72 */     return new BlockRenameFix(outputSchema, name)
/*    */       {
/*    */         protected String renameBlock(String block) {
/* 75 */           return (String)renamer.apply(block);
/*    */         }
/*    */       };
/*    */   }
/*    */   
/*    */   protected abstract String renameBlock(String paramString);
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\datafix\fixes\BlockRenameFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */