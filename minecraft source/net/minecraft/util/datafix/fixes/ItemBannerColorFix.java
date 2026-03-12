/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.google.common.collect.ImmutableMap;
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.OpticFinder;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.Type;
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import java.util.Objects;
/*    */ import java.util.Optional;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.util.datafix.schemas.NamespacedSchema;
/*    */ 
/*    */ 
/*    */ public class ItemBannerColorFix
/*    */   extends DataFix
/*    */ {
/* 22 */   public ItemBannerColorFix(Schema outputSchema, boolean changesType) { super(outputSchema, changesType); }
/*    */ 
/*    */ 
/*    */   
/*    */   public TypeRewriteRule makeRule() {
/* 27 */     Type<?> itemStackType = getInputSchema().getType(References.ITEM_STACK);
/*    */     
/* 29 */     OpticFinder<Pair<String, String>> idF = DSL.fieldFinder("id", DSL.named(References.ITEM_NAME.typeName(), NamespacedSchema.namespacedString()));
/* 30 */     OpticFinder<?> tagF = itemStackType.findField("tag");
/* 31 */     OpticFinder<?> blockEntityF = tagF.type().findField("BlockEntityTag");
/*    */     
/* 33 */     return fixTypeEverywhereTyped("ItemBannerColorFix", itemStackType, input -> {
/* 34 */           Optional<Pair<String, String>> id = input.getOptional(idF);
/* 35 */           if (id.isPresent() && Objects.equals(((Pair)id.get()).getSecond(), "minecraft:banner")) {
/* 36 */             Dynamic<?> rest = (Dynamic)input.get(DSL.remainderFinder());
/*    */             
/* 38 */             Optional<? extends Typed<?>> tagOpt = input.getOptionalTyped(tagF);
/* 39 */             if (tagOpt.isPresent()) {
/* 40 */               Typed<?> tag = (Typed)tagOpt.get();
/*    */               
/* 42 */               Optional<? extends Typed<?>> blockEntityOpt = tag.getOptionalTyped(blockEntityF);
/* 43 */               if (blockEntityOpt.isPresent()) {
/* 44 */                 Typed<?> blockEntity = (Typed)blockEntityOpt.get();
/* 45 */                 Dynamic<?> tagRest = (Dynamic)tag.get(DSL.remainderFinder());
/* 46 */                 Dynamic<?> blockEntityRest = (Dynamic)blockEntity.getOrCreate(DSL.remainderFinder());
/*    */                 
/* 48 */                 if (blockEntityRest.get("Base").asNumber().result().isPresent()) {
/*    */                   
/* 50 */                   rest = rest.set("Damage", rest.createShort((short)(blockEntityRest.get("Base").asInt(0) & 0xF)));
/*    */ 
/*    */                   
/* 53 */                   Optional<? extends Dynamic<?>> displayOptional = tagRest.get("display").result();
/* 54 */                   if (displayOptional.isPresent()) {
/* 55 */                     Dynamic<?> display = (Dynamic)displayOptional.get();
/* 56 */                     Dynamic<?> pickMarker = display.createMap(ImmutableMap.of(display.createString("Lore"), display.createList(Stream.of(display.createString("(+NBT")))));
/* 57 */                     if (Objects.equals(display, pickMarker)) {
/* 58 */                       return input.set(DSL.remainderFinder(), rest);
/*    */                     }
/*    */                   } 
/*    */ 
/*    */                   
/* 63 */                   blockEntityRest.remove("Base");
/* 64 */                   return input.set(DSL.remainderFinder(), rest).set(tagF, tag.set(blockEntityF, blockEntity.set(DSL.remainderFinder(), blockEntityRest)));
/*    */                 } 
/*    */               } 
/*    */             } 
/* 68 */             return input.set(DSL.remainderFinder(), rest);
/*    */           } 
/* 70 */           return input;
/*    */         });
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\datafix\fixes\ItemBannerColorFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */