/*     */ package net.minecraft.util.datafix.fixes;
/*     */ 
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
/*     */ import java.net.URI;
/*     */ import java.net.URISyntaxException;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Optional;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.util.datafix.ExtraDataFixUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class TextComponentHoverAndClickEventFix
/*     */   extends DataFix
/*     */ {
/*  32 */   public TextComponentHoverAndClickEventFix(Schema outputSchema) { super(outputSchema, true); }
/*     */ 
/*     */ 
/*     */   
/*     */   protected TypeRewriteRule makeRule() {
/*  37 */     Type<? extends Pair<String, ?>> hoverEventType = getInputSchema().getType(References.TEXT_COMPONENT).findFieldType("hoverEvent");
/*  38 */     return createFixer(getInputSchema().getTypeRaw(References.TEXT_COMPONENT), getOutputSchema().getType(References.TEXT_COMPONENT), hoverEventType);
/*     */   }
/*     */ 
/*     */   
/*     */   private <C1, C2, H extends Pair<String, ?>> TypeRewriteRule createFixer(Type<C1> oldRawTextComponentType, Type<C2> newTextComponentType, Type<H> hoverEventType) {
/*  43 */     Type<Pair<String, Either<Either<String, List<C1>>, Pair<Either<List<C1>, Unit>, Pair<Either<C1, Unit>, Pair<Either<H, Unit>, Dynamic<?>>>>>>> oldTextComponentType = DSL.named(References.TEXT_COMPONENT.typeName(), DSL.or(
/*  44 */           DSL.or(
/*  45 */             DSL.string(), 
/*  46 */             DSL.list(oldRawTextComponentType)), 
/*     */           
/*  48 */           DSL.and(
/*  49 */             DSL.optional(DSL.field("extra", DSL.list(oldRawTextComponentType))), 
/*  50 */             DSL.optional(DSL.field("separator", oldRawTextComponentType)), 
/*  51 */             DSL.optional(DSL.field("hoverEvent", hoverEventType)), 
/*  52 */             DSL.remainderType())));
/*     */ 
/*     */ 
/*     */     
/*  56 */     if (!oldTextComponentType.equals(getInputSchema().getType(References.TEXT_COMPONENT))) {
/*  57 */       throw new IllegalStateException("Text component type did not match, expected " + String.valueOf(oldTextComponentType) + " but got " + String.valueOf(getInputSchema().getType(References.TEXT_COMPONENT)));
/*     */     }
/*     */     
/*  60 */     Type<?> patchedInputType = ExtraDataFixUtils.patchSubType(oldTextComponentType, oldTextComponentType, newTextComponentType);
/*     */     
/*  62 */     return fixTypeEverywhere("TextComponentHoverAndClickEventFix", oldTextComponentType, newTextComponentType, ops -> ());
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
/*     */   private static Dynamic<?> fixTextComponent(Dynamic<?> dynamic) {
/*  85 */     return dynamic
/*  86 */       .renameAndFixField("hoverEvent", "hover_event", TextComponentHoverAndClickEventFix::fixHoverEvent)
/*     */       
/*  88 */       .renameAndFixField("clickEvent", "click_event", TextComponentHoverAndClickEventFix::fixClickEvent);
/*     */   }
/*     */   
/*     */   private static Dynamic<?> copyFields(Dynamic<?> target, Dynamic<?> source, String... fields) {
/*  92 */     for (String field : fields) {
/*  93 */       target = Dynamic.copyField(source, field, target, field);
/*     */     }
/*  95 */     return target;
/*     */   } private static Dynamic<?> fixHoverEvent(Dynamic<?> dynamic) {
/*     */     Optional<String> simpleId;
/*     */     Dynamic<?> contents, contents;
/*  99 */     String action = dynamic.get("action").asString("");
/* 100 */     switch (action) { case "show_text":
/*     */       
/*     */       case "show_item":
/* 103 */         contents = dynamic.get("contents").orElseEmptyMap();
/* 104 */         simpleId = contents.asString().result();
/* 105 */         return simpleId.isPresent() ? 
/* 106 */           dynamic.renameField("contents", "id") : 
/*     */           
/* 108 */           copyFields(dynamic.remove("contents"), contents, new String[] { "id", "count", "components" });
/*     */       
/*     */       case "show_entity":
/* 111 */         contents = dynamic.get("contents").orElseEmptyMap(); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 116 */     return dynamic;
/*     */   }
/*     */   private static <T> Dynamic<T> fixClickEvent(Dynamic<T> dynamic) {
/*     */     int page;
/*     */     Integer oldPage;
/* 121 */     String action = dynamic.get("action").asString("");
/* 122 */     String value = dynamic.get("value").asString("");
/* 123 */     switch (action) { case "open_url": return 
/*     */ 
/*     */           
/* 126 */           !validateUri(value) ? 
/* 127 */           null : 
/*     */           
/* 129 */           dynamic.renameField("value", "url");
/*     */       case "open_file": 
/*     */       case "run_command":
/*     */       case "suggest_command":
/* 133 */         return !validateChat(value) ? 
/* 134 */           null : 
/*     */           
/* 136 */           dynamic.renameField("value", "command");
/*     */       
/*     */       case "change_page":
/* 139 */         oldPage = (Integer)dynamic.get("value").result().map(TextComponentHoverAndClickEventFix::parseOldPage).orElse(null);
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 144 */         page = Math.max(oldPage.intValue(), 1);
/* 145 */         return (oldPage == null) ? null : dynamic.remove("value").set("page", dynamic.createInt(page)); }
/*     */     
/* 147 */     return dynamic;
/*     */   }
/*     */ 
/*     */   
/*     */   private static Integer parseOldPage(Dynamic<?> value) {
/* 152 */     Optional<Number> numberValue = value.asNumber().result();
/*     */     
/* 154 */     if (numberValue.isPresent()) {
/* 155 */       return Integer.valueOf(((Number)numberValue.get()).intValue());
/*     */     }
/*     */     try {
/* 158 */       return Integer.valueOf(Integer.parseInt(value.asString("")));
/* 159 */     } catch (Exception ignored) {
/* 160 */       return null;
/*     */     } 
/*     */   }
/*     */   
/*     */   private static boolean validateUri(String uri) {
/*     */     try {
/* 166 */       URI parsedUri = new URI(uri);
/* 167 */       String scheme = parsedUri.getScheme();
/* 168 */       if (scheme == null) {
/* 169 */         return false;
/*     */       }
/* 171 */       String protocol = scheme.toLowerCase(Locale.ROOT);
/* 172 */       return ("http".equals(protocol) || "https".equals(protocol));
/* 173 */     } catch (URISyntaxException e) {
/* 174 */       return false;
/*     */     } 
/*     */   }
/*     */   
/*     */   private static boolean validateChat(String string) {
/* 179 */     for (int i = 0; i < string.length(); i++) {
/* 180 */       char c = string.charAt(i);
/* 181 */       if (c == '§' || c < ' ' || c == '') {
/* 182 */         return false;
/*     */       }
/*     */     } 
/* 185 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\datafix\fixes\TextComponentHoverAndClickEventFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */