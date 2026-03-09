/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.Type;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import com.mojang.serialization.OptionalDynamic;
/*    */ import java.util.Arrays;
/*    */ import java.util.function.Function;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class EntityProjectileOwnerFix
/*    */   extends DataFix
/*    */ {
/* 19 */   public EntityProjectileOwnerFix(Schema outputSchema) { super(outputSchema, false); }
/*    */ 
/*    */ 
/*    */   
/*    */   protected TypeRewriteRule makeRule() {
/* 24 */     Schema inputSchema = getInputSchema();
/* 25 */     return fixTypeEverywhereTyped("EntityProjectileOwner", inputSchema.getType(References.ENTITY), this::updateProjectiles);
/*    */   }
/*    */   
/*    */   private Typed<?> updateProjectiles(Typed<?> input) {
/* 29 */     input = updateEntity(input, "minecraft:egg", this::updateOwnerThrowable);
/* 30 */     input = updateEntity(input, "minecraft:ender_pearl", this::updateOwnerThrowable);
/* 31 */     input = updateEntity(input, "minecraft:experience_bottle", this::updateOwnerThrowable);
/* 32 */     input = updateEntity(input, "minecraft:snowball", this::updateOwnerThrowable);
/* 33 */     input = updateEntity(input, "minecraft:potion", this::updateOwnerThrowable);
/* 34 */     input = updateEntity(input, "minecraft:llama_spit", this::updateOwnerLlamaSpit);
/* 35 */     input = updateEntity(input, "minecraft:arrow", this::updateOwnerArrow);
/* 36 */     input = updateEntity(input, "minecraft:spectral_arrow", this::updateOwnerArrow);
/* 37 */     return updateEntity(input, "minecraft:trident", this::updateOwnerArrow);
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
/*    */   private Dynamic<?> updateOwnerArrow(Dynamic<?> tag) {
/* 51 */     long mostSignificantBits = tag.get("OwnerUUIDMost").asLong(0L);
/* 52 */     long leastSignificantBits = tag.get("OwnerUUIDLeast").asLong(0L);
/*    */     
/* 54 */     return setUUID(tag, mostSignificantBits, leastSignificantBits).remove("OwnerUUIDMost").remove("OwnerUUIDLeast");
/*    */   }
/*    */   
/*    */   private Dynamic<?> updateOwnerLlamaSpit(Dynamic<?> tag) {
/* 58 */     OptionalDynamic<?> owner = tag.get("Owner");
/* 59 */     long mostSignificantBits = owner.get("OwnerUUIDMost").asLong(0L);
/* 60 */     long leastSignificantBits = owner.get("OwnerUUIDLeast").asLong(0L);
/*    */     
/* 62 */     return setUUID(tag, mostSignificantBits, leastSignificantBits).remove("Owner");
/*    */   }
/*    */   
/*    */   private Dynamic<?> updateOwnerThrowable(Dynamic<?> tag) {
/* 66 */     String ownerKey = "owner";
/* 67 */     OptionalDynamic<?> owner = tag.get("owner");
/* 68 */     long mostSignificantBits = owner.get("M").asLong(0L);
/* 69 */     long leastSignificantBits = owner.get("L").asLong(0L);
/*    */     
/* 71 */     return setUUID(tag, mostSignificantBits, leastSignificantBits).remove("owner");
/*    */   }
/*    */   
/*    */   private Dynamic<?> setUUID(Dynamic<?> tag, long mostSignificantBits, long leastSignificantBits) {
/* 75 */     String name = "OwnerUUID";
/* 76 */     if (mostSignificantBits != 0L && leastSignificantBits != 0L) {
/* 77 */       return tag.set("OwnerUUID", tag.createIntList(Arrays.stream(createUUIDArray(mostSignificantBits, leastSignificantBits))));
/*    */     }
/* 79 */     return tag;
/*    */   }
/*    */ 
/*    */   
/* 83 */   private static int[] createUUIDArray(long mostSignificantBits, long leastSignificantBits) { return new int[] { (int)(mostSignificantBits >> 32), (int)mostSignificantBits, (int)(leastSignificantBits >> 32), (int)leastSignificantBits }; }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private Typed<?> updateEntity(Typed<?> input, String name, Function<Dynamic<?>, Dynamic<?>> function) {
/* 92 */     Type<?> oldType = getInputSchema().getChoiceType(References.ENTITY, name);
/* 93 */     Type<?> newType = getOutputSchema().getChoiceType(References.ENTITY, name);
/* 94 */     return input.updateTyped(DSL.namedChoice(name, oldType), newType, entity -> entity.update(DSL.remainderFinder(), function));
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\datafix\fixes\EntityProjectileOwnerFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */