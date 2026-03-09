/*    */ package net.minecraft.util.datafix.fixes;
/*    */ import com.google.common.collect.ImmutableSet;
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.DataFixUtils;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import java.util.Objects;
/*    */ import java.util.Set;
/*    */ 
/*    */ public class WallPropertyFix extends DataFix {
/* 14 */   private static final Set<String> WALL_BLOCKS = ImmutableSet.of("minecraft:andesite_wall", "minecraft:brick_wall", "minecraft:cobblestone_wall", "minecraft:diorite_wall", "minecraft:end_stone_brick_wall", "minecraft:granite_wall", new String[] { "minecraft:mossy_cobblestone_wall", "minecraft:mossy_stone_brick_wall", "minecraft:nether_brick_wall", "minecraft:prismarine_wall", "minecraft:red_nether_brick_wall", "minecraft:red_sandstone_wall", "minecraft:sandstone_wall", "minecraft:stone_brick_wall" });
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
/*    */ 
/*    */   
/* 32 */   public WallPropertyFix(Schema outputSchema, boolean changesType) { super(outputSchema, changesType); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 37 */   public TypeRewriteRule makeRule() { return fixTypeEverywhereTyped("WallPropertyFix", getInputSchema().getType(References.BLOCK_STATE), input -> input.update(DSL.remainderFinder(), WallPropertyFix::upgradeBlockStateTag)); }
/*    */ 
/*    */ 
/*    */   
/* 41 */   private static String mapProperty(String value) { return "true".equals(value) ? "low" : "none"; }
/*    */ 
/*    */ 
/*    */   
/* 45 */   private static <T> Dynamic<T> fixWallProperty(Dynamic<T> state, String property) { return state.update(property, value -> { Objects.requireNonNull(value); return (Dynamic)DataFixUtils.orElse(value.asString().result().map(WallPropertyFix::mapProperty).map(value::createString), value);
/*    */         }); }
/*    */   
/*    */   private static <T> Dynamic<T> upgradeBlockStateTag(Dynamic<T> state) {
/* 49 */     Objects.requireNonNull(WALL_BLOCKS); boolean isWall = state.get("Name").asString().result().filter(WALL_BLOCKS::contains).isPresent();
/* 50 */     if (!isWall) {
/* 51 */       return state;
/*    */     }
/*    */     
/* 54 */     return state.update("Properties", properties -> {
/* 55 */           Dynamic<?> newState = fixWallProperty(properties, "east");
/* 56 */           newState = fixWallProperty(newState, "west");
/* 57 */           newState = fixWallProperty(newState, "north");
/* 58 */           return fixWallProperty(newState, "south");
/*    */         });
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\datafix\fixes\WallPropertyFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */