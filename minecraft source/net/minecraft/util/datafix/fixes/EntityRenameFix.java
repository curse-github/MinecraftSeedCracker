/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.Type;
/*    */ import com.mojang.datafixers.types.templates.TaggedChoice;
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import com.mojang.serialization.DynamicOps;
/*    */ import java.util.Locale;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.util.Util;
/*    */ import net.minecraft.util.datafix.ExtraDataFixUtils;
/*    */ 
/*    */ public abstract class EntityRenameFix
/*    */   extends DataFix {
/*    */   protected final String name;
/*    */   
/*    */   public EntityRenameFix(String name, Schema outputSchema, boolean changesType) {
/* 21 */     super(outputSchema, changesType);
/* 22 */     this.name = name;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public TypeRewriteRule makeRule() {
/* 28 */     TaggedChoice.TaggedChoiceType<String> oldType = getInputSchema().findChoiceType(References.ENTITY);
/* 29 */     TaggedChoice.TaggedChoiceType<String> newType = getOutputSchema().findChoiceType(References.ENTITY);
/*    */     
/* 31 */     Function<String, Type<?>> patchedInputTypes = Util.memoize(name -> {
/* 32 */           Type<?> type = (Type)oldType.types().get(name);
/* 33 */           return ExtraDataFixUtils.patchSubType(type, oldType, newType);
/*    */         });
/*    */     
/* 36 */     return fixTypeEverywhere(this.name, oldType, newType, ops -> ());
/*    */   }
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
/* 53 */   private <A> Typed<A> getEntity(Object input, DynamicOps<?> ops, Type<A> oldEntityType) { return new Typed(oldEntityType, ops, input); }
/*    */   
/*    */   protected abstract Pair<String, Typed<?>> fix(String paramString, Typed<?> paramTyped);
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\datafix\fixes\EntityRenameFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */