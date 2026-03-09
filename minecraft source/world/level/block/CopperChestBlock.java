/*     */ package net.minecraft.world.level.block;
/*     */ import com.google.common.collect.BiMap;
/*     */ import com.mojang.datafixers.util.Function4;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import java.util.function.Supplier;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.registries.BuiltInRegistries;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.item.context.BlockPlaceContext;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.ScheduledTickAccess;
/*     */ import net.minecraft.world.level.block.entity.BlockEntityType;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.properties.ChestType;
/*     */ 
/*     */ public class CopperChestBlock extends ChestBlock {
/*  25 */   public static final MapCodec<CopperChestBlock> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(WeatheringCopper.WeatherState.CODEC
/*  26 */         .fieldOf("weathering_state").forGetter(CopperChestBlock::getState), BuiltInRegistries.SOUND_EVENT
/*  27 */         .byNameCodec().fieldOf("open_sound").forGetter(ChestBlock::getOpenChestSound), BuiltInRegistries.SOUND_EVENT
/*  28 */         .byNameCodec().fieldOf("close_sound").forGetter(ChestBlock::getCloseChestSound), 
/*  29 */         propertiesCodec())
/*  30 */       .apply(i, CopperChestBlock::new));
/*     */   
/*  32 */   private static final Map<Block, Supplier<Block>> COPPER_TO_COPPER_CHEST_MAPPING = Map.of(Blocks.COPPER_BLOCK, () -> 
/*  33 */       Blocks.COPPER_CHEST, Blocks.EXPOSED_COPPER, () -> 
/*  34 */       Blocks.EXPOSED_COPPER_CHEST, Blocks.WEATHERED_COPPER, () -> 
/*  35 */       Blocks.WEATHERED_COPPER_CHEST, Blocks.OXIDIZED_COPPER, () -> 
/*  36 */       Blocks.OXIDIZED_COPPER_CHEST, Blocks.WAXED_COPPER_BLOCK, () -> 
/*  37 */       Blocks.COPPER_CHEST, Blocks.WAXED_EXPOSED_COPPER, () -> 
/*  38 */       Blocks.EXPOSED_COPPER_CHEST, Blocks.WAXED_WEATHERED_COPPER, () -> 
/*  39 */       Blocks.WEATHERED_COPPER_CHEST, Blocks.WAXED_OXIDIZED_COPPER, () -> 
/*  40 */       Blocks.OXIDIZED_COPPER_CHEST);
/*     */   
/*     */   private final WeatheringCopper.WeatherState weatherState;
/*     */   
/*  44 */   public MapCodec<? extends CopperChestBlock> codec() { return CODEC; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CopperChestBlock(WeatheringCopper.WeatherState weatherState, SoundEvent openSound, SoundEvent closeSound, BlockBehaviour.Properties properties) {
/*  50 */     super(() -> BlockEntityType.CHEST, openSound, closeSound, properties);
/*  51 */     this.weatherState = weatherState;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  56 */   public boolean chestCanConnectTo(BlockState blockState) { return (blockState.is(BlockTags.COPPER_CHESTS) && blockState.hasProperty(ChestBlock.TYPE)); }
/*     */ 
/*     */ 
/*     */   
/*     */   public BlockState getStateForPlacement(BlockPlaceContext context) {
/*  61 */     BlockState state = super.getStateForPlacement(context);
/*  62 */     return getLeastOxidizedChestOfConnectedBlocks(state, context.getLevel(), context.getClickedPos());
/*     */   }
/*     */   
/*     */   private static BlockState getLeastOxidizedChestOfConnectedBlocks(BlockState state, Level level, BlockPos pos) {
/*  66 */     BlockState connectedState = level.getBlockState(pos.relative(getConnectedDirection(state)));
/*  67 */     if (!((ChestType)state.getValue(ChestBlock.TYPE)).equals(ChestType.SINGLE)) { Block block = state.getBlock(); if (block instanceof CopperChestBlock) { CopperChestBlock copperChestBlock = (CopperChestBlock)block; block = connectedState.getBlock(); if (block instanceof CopperChestBlock) { CopperChestBlock connectedCopperChestBlock = (CopperChestBlock)block;
/*  68 */           BlockState updatedBlockState = state;
/*  69 */           BlockState connectedPredictedBlockState = connectedState;
/*     */           
/*  71 */           if (copperChestBlock.isWaxed() != connectedCopperChestBlock.isWaxed()) {
/*  72 */             updatedBlockState = (BlockState)unwaxBlock(copperChestBlock, state).orElse(updatedBlockState);
/*  73 */             connectedPredictedBlockState = (BlockState)unwaxBlock(connectedCopperChestBlock, connectedState).orElse(connectedPredictedBlockState);
/*     */           } 
/*     */           
/*  76 */           Block leastOxidizedBlock = (copperChestBlock.weatherState.ordinal() <= connectedCopperChestBlock.weatherState.ordinal()) ? updatedBlockState.getBlock() : connectedPredictedBlockState.getBlock();
/*  77 */           return leastOxidizedBlock.withPropertiesOf(updatedBlockState); }  }
/*     */        }
/*  79 */      return state;
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction directionToNeighbour, BlockPos neighbourPos, BlockState neighbourState, RandomSource random) {
/*  84 */     BlockState blockState = super.updateShape(state, level, ticks, pos, directionToNeighbour, neighbourPos, neighbourState, random);
/*  85 */     if (chestCanConnectTo(neighbourState)) {
/*  86 */       ChestType chestType = (ChestType)blockState.getValue(ChestBlock.TYPE);
/*  87 */       if (!chestType.equals(ChestType.SINGLE) && getConnectedDirection(blockState) == directionToNeighbour) {
/*  88 */         return neighbourState.getBlock().withPropertiesOf(blockState);
/*     */       }
/*     */     } 
/*  91 */     return blockState;
/*     */   }
/*     */   
/*     */   private static Optional<BlockState> unwaxBlock(CopperChestBlock copperChestBlock, BlockState state) {
/*  95 */     if (!copperChestBlock.isWaxed()) {
/*  96 */       return Optional.of(state);
/*     */     }
/*  98 */     return Optional.ofNullable((Block)((BiMap)HoneycombItem.WAX_OFF_BY_BLOCK.get()).get(state.getBlock())).map(b -> b.withPropertiesOf(state));
/*     */   }
/*     */ 
/*     */   
/* 102 */   public WeatheringCopper.WeatherState getState() { return this.weatherState; }
/*     */ 
/*     */   
/*     */   public static BlockState getFromCopperBlock(Block copperBlock, Direction facing, Level level, BlockPos pos) {
/* 106 */     Objects.requireNonNull(Blocks.COPPER_CHEST); CopperChestBlock block = (CopperChestBlock)((Supplier)COPPER_TO_COPPER_CHEST_MAPPING.getOrDefault(copperBlock, Blocks.COPPER_CHEST::asBlock)).get();
/* 107 */     ChestType chestType = block.getChestType(level, pos, facing);
/* 108 */     BlockState state = (BlockState)((BlockState)block.defaultBlockState().setValue(FACING, facing)).setValue(TYPE, chestType);
/* 109 */     return getLeastOxidizedChestOfConnectedBlocks(state, level, pos);
/*     */   }
/*     */ 
/*     */   
/* 113 */   public boolean isWaxed() { return true; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 118 */   public boolean shouldChangedStateKeepBlockEntity(BlockState oldState) { return oldState.is(BlockTags.COPPER_CHESTS); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\CopperChestBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */