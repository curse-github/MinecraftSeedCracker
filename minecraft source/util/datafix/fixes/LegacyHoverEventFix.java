/*     */ package net.minecraft.util.datafix.fixes;
/*     */ 
/*     */ import com.google.gson.JsonElement;
/*     */ import com.mojang.datafixers.DSL;
/*     */ import com.mojang.datafixers.DataFix;
/*     */ import com.mojang.datafixers.TypeRewriteRule;
/*     */ import com.mojang.datafixers.schemas.Schema;
/*     */ import com.mojang.datafixers.types.Type;
/*     */ import com.mojang.datafixers.util.Either;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import com.mojang.datafixers.util.Unit;
/*     */ import com.mojang.serialization.Dynamic;
/*     */ import com.mojang.serialization.DynamicOps;
/*     */ import com.mojang.serialization.JavaOps;
/*     */ import com.mojang.serialization.JsonOps;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.util.GsonHelper;
/*     */ import net.minecraft.util.Util;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class LegacyHoverEventFix
/*     */   extends DataFix
/*     */ {
/*  31 */   public LegacyHoverEventFix(Schema outputSchema) { super(outputSchema, false); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected TypeRewriteRule makeRule() {
/*  37 */     Type<? extends Pair<String, ?>> hoverEventType = getInputSchema().getType(References.TEXT_COMPONENT).findFieldType("hoverEvent");
/*  38 */     return createFixer(getInputSchema().getTypeRaw(References.TEXT_COMPONENT), hoverEventType);
/*     */   }
/*     */   
/*     */   private <C, H extends Pair<String, ?>> TypeRewriteRule createFixer(Type<C> rawTextComponentType, Type<H> hoverEventType) {
/*  42 */     Type<Pair<String, Either<Either<String, List<C>>, Pair<Either<List<C>, Unit>, Pair<Either<C, Unit>, Pair<Either<H, Unit>, Dynamic<?>>>>>>> textComponentType = DSL.named(References.TEXT_COMPONENT.typeName(), DSL.or(
/*  43 */           DSL.or(
/*  44 */             DSL.string(), 
/*  45 */             DSL.list(rawTextComponentType)), 
/*     */           
/*  47 */           DSL.and(
/*  48 */             DSL.optional(DSL.field("extra", DSL.list(rawTextComponentType))), 
/*  49 */             DSL.optional(DSL.field("separator", rawTextComponentType)), 
/*  50 */             DSL.optional(DSL.field("hoverEvent", hoverEventType)), 
/*  51 */             DSL.remainderType())));
/*     */ 
/*     */ 
/*     */     
/*  55 */     if (!textComponentType.equals(getInputSchema().getType(References.TEXT_COMPONENT))) {
/*  56 */       throw new IllegalStateException("Text component type did not match, expected " + String.valueOf(textComponentType) + " but got " + String.valueOf(getInputSchema().getType(References.TEXT_COMPONENT)));
/*     */     }
/*     */     
/*  59 */     return fixTypeEverywhere("LegacyHoverEventFix", textComponentType, ops -> ());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private <H> H fixHoverEvent(Type<H> hoverEventType, String action, Dynamic<?> oldHoverEvent) {
/*  88 */     if ("show_text".equals(action))
/*     */     {
/*  90 */       return (H)fixShowTextHover(hoverEventType, oldHoverEvent);
/*     */     }
/*  92 */     return (H)createPlaceholderHover(hoverEventType, oldHoverEvent);
/*     */   }
/*     */ 
/*     */   
/*     */   private static <H> H fixShowTextHover(Type<H> hoverEventType, Dynamic<?> oldHoverEvent) {
/*  97 */     Dynamic<?> newHoverEvent = oldHoverEvent.renameField("value", "contents");
/*  98 */     return (H)Util.readTypedOrThrow(hoverEventType, newHoverEvent).getValue();
/*     */   }
/*     */   
/*     */   private static <H> H createPlaceholderHover(Type<H> hoverEventType, Dynamic<?> oldHoverEvent) {
/* 102 */     JsonElement oldJson = (JsonElement)oldHoverEvent.convert(JsonOps.INSTANCE).getValue();
/* 103 */     Dynamic<?> placeholderHoverEvent = new Dynamic<?>(JavaOps.INSTANCE, Map.of("action", "show_text", "contents", 
/*     */           
/* 105 */           Map.of("text", "Legacy hoverEvent: " + 
/* 106 */             GsonHelper.toStableString(oldJson))));
/*     */ 
/*     */     
/* 109 */     return (H)Util.readTypedOrThrow(hoverEventType, placeholderHoverEvent).getValue();
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\datafix\fixes\LegacyHoverEventFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */