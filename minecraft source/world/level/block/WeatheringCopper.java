/*     */ package net.minecraft.world.level.block;
/*     */ 
/*     */ import com.google.common.base.Suppliers;
/*     */ import com.google.common.collect.BiMap;
/*     */ import com.google.common.collect.ImmutableBiMap;
/*     */ import com.mojang.serialization.Codec;
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import java.util.Optional;
/*     */ import java.util.function.IntFunction;
/*     */ import java.util.function.Supplier;
/*     */ import net.minecraft.network.codec.ByteBufCodecs;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ import net.minecraft.util.ByIdMap;
/*     */ import net.minecraft.util.StringRepresentable;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ 
/*     */ public interface WeatheringCopper
/*     */   extends ChangeOverTimeBlock<WeatheringCopper.WeatherState> {
/*  19 */   public static final Supplier<BiMap<Block, Block>> NEXT_BY_BLOCK = Suppliers.memoize(() -> ImmutableBiMap.builder()
/*     */       
/*  21 */       .put(Blocks.COPPER_BLOCK, Blocks.EXPOSED_COPPER)
/*  22 */       .put(Blocks.EXPOSED_COPPER, Blocks.WEATHERED_COPPER)
/*  23 */       .put(Blocks.WEATHERED_COPPER, Blocks.OXIDIZED_COPPER)
/*     */       
/*  25 */       .put(Blocks.CUT_COPPER, Blocks.EXPOSED_CUT_COPPER)
/*  26 */       .put(Blocks.EXPOSED_CUT_COPPER, Blocks.WEATHERED_CUT_COPPER)
/*  27 */       .put(Blocks.WEATHERED_CUT_COPPER, Blocks.OXIDIZED_CUT_COPPER)
/*     */       
/*  29 */       .put(Blocks.CHISELED_COPPER, Blocks.EXPOSED_CHISELED_COPPER)
/*  30 */       .put(Blocks.EXPOSED_CHISELED_COPPER, Blocks.WEATHERED_CHISELED_COPPER)
/*  31 */       .put(Blocks.WEATHERED_CHISELED_COPPER, Blocks.OXIDIZED_CHISELED_COPPER)
/*     */       
/*  33 */       .put(Blocks.CUT_COPPER_SLAB, Blocks.EXPOSED_CUT_COPPER_SLAB)
/*  34 */       .put(Blocks.EXPOSED_CUT_COPPER_SLAB, Blocks.WEATHERED_CUT_COPPER_SLAB)
/*  35 */       .put(Blocks.WEATHERED_CUT_COPPER_SLAB, Blocks.OXIDIZED_CUT_COPPER_SLAB)
/*     */       
/*  37 */       .put(Blocks.CUT_COPPER_STAIRS, Blocks.EXPOSED_CUT_COPPER_STAIRS)
/*  38 */       .put(Blocks.EXPOSED_CUT_COPPER_STAIRS, Blocks.WEATHERED_CUT_COPPER_STAIRS)
/*  39 */       .put(Blocks.WEATHERED_CUT_COPPER_STAIRS, Blocks.OXIDIZED_CUT_COPPER_STAIRS)
/*     */       
/*  41 */       .put(Blocks.COPPER_DOOR, Blocks.EXPOSED_COPPER_DOOR)
/*  42 */       .put(Blocks.EXPOSED_COPPER_DOOR, Blocks.WEATHERED_COPPER_DOOR)
/*  43 */       .put(Blocks.WEATHERED_COPPER_DOOR, Blocks.OXIDIZED_COPPER_DOOR)
/*     */       
/*  45 */       .put(Blocks.COPPER_TRAPDOOR, Blocks.EXPOSED_COPPER_TRAPDOOR)
/*  46 */       .put(Blocks.EXPOSED_COPPER_TRAPDOOR, Blocks.WEATHERED_COPPER_TRAPDOOR)
/*  47 */       .put(Blocks.WEATHERED_COPPER_TRAPDOOR, Blocks.OXIDIZED_COPPER_TRAPDOOR)
/*     */       
/*  49 */       .putAll(Blocks.COPPER_BARS.weatheringMapping())
/*     */       
/*  51 */       .put(Blocks.COPPER_GRATE, Blocks.EXPOSED_COPPER_GRATE)
/*  52 */       .put(Blocks.EXPOSED_COPPER_GRATE, Blocks.WEATHERED_COPPER_GRATE)
/*  53 */       .put(Blocks.WEATHERED_COPPER_GRATE, Blocks.OXIDIZED_COPPER_GRATE)
/*     */       
/*  55 */       .put(Blocks.COPPER_BULB, Blocks.EXPOSED_COPPER_BULB)
/*  56 */       .put(Blocks.EXPOSED_COPPER_BULB, Blocks.WEATHERED_COPPER_BULB)
/*  57 */       .put(Blocks.WEATHERED_COPPER_BULB, Blocks.OXIDIZED_COPPER_BULB)
/*     */       
/*  59 */       .putAll(Blocks.COPPER_LANTERN.weatheringMapping())
/*     */       
/*  61 */       .put(Blocks.COPPER_CHEST, Blocks.EXPOSED_COPPER_CHEST)
/*  62 */       .put(Blocks.EXPOSED_COPPER_CHEST, Blocks.WEATHERED_COPPER_CHEST)
/*  63 */       .put(Blocks.WEATHERED_COPPER_CHEST, Blocks.OXIDIZED_COPPER_CHEST)
/*     */       
/*  65 */       .put(Blocks.COPPER_GOLEM_STATUE, Blocks.EXPOSED_COPPER_GOLEM_STATUE)
/*  66 */       .put(Blocks.EXPOSED_COPPER_GOLEM_STATUE, Blocks.WEATHERED_COPPER_GOLEM_STATUE)
/*  67 */       .put(Blocks.WEATHERED_COPPER_GOLEM_STATUE, Blocks.OXIDIZED_COPPER_GOLEM_STATUE)
/*     */       
/*  69 */       .put(Blocks.LIGHTNING_ROD, Blocks.EXPOSED_LIGHTNING_ROD)
/*  70 */       .put(Blocks.EXPOSED_LIGHTNING_ROD, Blocks.WEATHERED_LIGHTNING_ROD)
/*  71 */       .put(Blocks.WEATHERED_LIGHTNING_ROD, Blocks.OXIDIZED_LIGHTNING_ROD)
/*     */       
/*  73 */       .putAll(Blocks.COPPER_CHAIN.weatheringMapping())
/*  74 */       .build());
/*     */   
/*  76 */   public static final Supplier<BiMap<Block, Block>> PREVIOUS_BY_BLOCK = Suppliers.memoize(() -> ((BiMap)NEXT_BY_BLOCK.get()).inverse());
/*     */ 
/*     */   
/*  79 */   static Optional<Block> getPrevious(Block block) { return Optional.ofNullable((Block)((BiMap)PREVIOUS_BY_BLOCK.get()).get(block)); }
/*     */ 
/*     */   
/*     */   static Block getFirst(Block block) {
/*  83 */     Block candiate = block;
/*  84 */     Block previous = (Block)((BiMap)PREVIOUS_BY_BLOCK.get()).get(candiate);
/*  85 */     while (previous != null) {
/*  86 */       candiate = previous;
/*  87 */       previous = (Block)((BiMap)PREVIOUS_BY_BLOCK.get()).get(candiate);
/*     */     } 
/*  89 */     return candiate;
/*     */   }
/*     */ 
/*     */   
/*  93 */   static Optional<BlockState> getPrevious(BlockState state) { return getPrevious(state.getBlock()).map(s -> s.withPropertiesOf(state)); }
/*     */ 
/*     */ 
/*     */   
/*  97 */   static Optional<Block> getNext(Block block) { return Optional.ofNullable((Block)((BiMap)NEXT_BY_BLOCK.get()).get(block)); }
/*     */ 
/*     */ 
/*     */   
/* 101 */   static BlockState getFirst(BlockState state) { return getFirst(state.getBlock()).withPropertiesOf(state); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 106 */   default Optional<BlockState> getNext(BlockState state) { return getNext(state.getBlock()).map(s -> s.withPropertiesOf(state)); }
/*     */ 
/*     */ 
/*     */   
/*     */   default float getChanceModifier() {
/* 111 */     if (getAge() == WeatherState.UNAFFECTED) {
/* 112 */       return 0.75F;
/*     */     }
/* 114 */     return 1.0F;
/*     */   }
/*     */   
/*     */   public enum WeatherState implements StringRepresentable {
/*     */     public static final IntFunction<WeatherState> BY_ID;
/*     */     public static final Codec<WeatherState> CODEC;
/* 120 */     UNAFFECTED("unaffected"),
/* 121 */     EXPOSED("exposed"),
/* 122 */     WEATHERED("weathered"),
/* 123 */     OXIDIZED("oxidized");
/*     */     static  {
/* 125 */       BY_ID = ByIdMap.continuous(Enum::ordinal, values(), ByIdMap.OutOfBoundsStrategy.CLAMP);
/* 126 */       CODEC = StringRepresentable.fromEnum(WeatherState::values);
/* 127 */       STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, Enum::ordinal);
/*     */     }
/*     */     public static final StreamCodec<ByteBuf, WeatherState> STREAM_CODEC;
/*     */     private final String name;
/*     */     
/* 132 */     WeatherState(String name) { this.name = name; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 137 */     public String getSerializedName() { return this.name; }
/*     */ 
/*     */ 
/*     */     
/* 141 */     public WeatherState next() { return (WeatherState)BY_ID.apply(ordinal() + 1); }
/*     */ 
/*     */ 
/*     */     
/* 145 */     public WeatherState previous() { return (WeatherState)BY_ID.apply(ordinal() - 1); }
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\WeatheringCopper.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */