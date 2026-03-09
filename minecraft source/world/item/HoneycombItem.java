/*     */ package net.minecraft.world.item;
/*     */ 
/*     */ import com.google.common.base.Suppliers;
/*     */ import com.google.common.collect.BiMap;
/*     */ import com.google.common.collect.ImmutableBiMap;
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import java.util.Optional;
/*     */ import java.util.function.Supplier;
/*     */ import net.minecraft.advancements.CriteriaTriggers;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.data.recipes.RecipeCategory;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.context.UseOnContext;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.ChestBlock;
/*     */ import net.minecraft.world.level.block.entity.SignBlockEntity;
/*     */ import net.minecraft.world.level.block.entity.SignText;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.properties.ChestType;
/*     */ import net.minecraft.world.level.gameevent.GameEvent;
/*     */ 
/*     */ public class HoneycombItem
/*     */   extends Item
/*     */   implements SignApplicator {
/*  30 */   public static final Supplier<BiMap<Block, Block>> WAXABLES = Suppliers.memoize(() -> ImmutableBiMap.builder()
/*     */       
/*  32 */       .put(Blocks.COPPER_BLOCK, Blocks.WAXED_COPPER_BLOCK)
/*  33 */       .put(Blocks.EXPOSED_COPPER, Blocks.WAXED_EXPOSED_COPPER)
/*  34 */       .put(Blocks.WEATHERED_COPPER, Blocks.WAXED_WEATHERED_COPPER)
/*  35 */       .put(Blocks.OXIDIZED_COPPER, Blocks.WAXED_OXIDIZED_COPPER)
/*     */       
/*  37 */       .put(Blocks.CUT_COPPER, Blocks.WAXED_CUT_COPPER)
/*  38 */       .put(Blocks.EXPOSED_CUT_COPPER, Blocks.WAXED_EXPOSED_CUT_COPPER)
/*  39 */       .put(Blocks.WEATHERED_CUT_COPPER, Blocks.WAXED_WEATHERED_CUT_COPPER)
/*  40 */       .put(Blocks.OXIDIZED_CUT_COPPER, Blocks.WAXED_OXIDIZED_CUT_COPPER)
/*     */       
/*  42 */       .put(Blocks.CUT_COPPER_SLAB, Blocks.WAXED_CUT_COPPER_SLAB)
/*  43 */       .put(Blocks.EXPOSED_CUT_COPPER_SLAB, Blocks.WAXED_EXPOSED_CUT_COPPER_SLAB)
/*  44 */       .put(Blocks.WEATHERED_CUT_COPPER_SLAB, Blocks.WAXED_WEATHERED_CUT_COPPER_SLAB)
/*  45 */       .put(Blocks.OXIDIZED_CUT_COPPER_SLAB, Blocks.WAXED_OXIDIZED_CUT_COPPER_SLAB)
/*     */       
/*  47 */       .put(Blocks.CUT_COPPER_STAIRS, Blocks.WAXED_CUT_COPPER_STAIRS)
/*  48 */       .put(Blocks.EXPOSED_CUT_COPPER_STAIRS, Blocks.WAXED_EXPOSED_CUT_COPPER_STAIRS)
/*  49 */       .put(Blocks.WEATHERED_CUT_COPPER_STAIRS, Blocks.WAXED_WEATHERED_CUT_COPPER_STAIRS)
/*  50 */       .put(Blocks.OXIDIZED_CUT_COPPER_STAIRS, Blocks.WAXED_OXIDIZED_CUT_COPPER_STAIRS)
/*     */       
/*  52 */       .put(Blocks.CHISELED_COPPER, Blocks.WAXED_CHISELED_COPPER)
/*  53 */       .put(Blocks.EXPOSED_CHISELED_COPPER, Blocks.WAXED_EXPOSED_CHISELED_COPPER)
/*  54 */       .put(Blocks.WEATHERED_CHISELED_COPPER, Blocks.WAXED_WEATHERED_CHISELED_COPPER)
/*  55 */       .put(Blocks.OXIDIZED_CHISELED_COPPER, Blocks.WAXED_OXIDIZED_CHISELED_COPPER)
/*     */       
/*  57 */       .put(Blocks.COPPER_DOOR, Blocks.WAXED_COPPER_DOOR)
/*  58 */       .put(Blocks.EXPOSED_COPPER_DOOR, Blocks.WAXED_EXPOSED_COPPER_DOOR)
/*  59 */       .put(Blocks.WEATHERED_COPPER_DOOR, Blocks.WAXED_WEATHERED_COPPER_DOOR)
/*  60 */       .put(Blocks.OXIDIZED_COPPER_DOOR, Blocks.WAXED_OXIDIZED_COPPER_DOOR)
/*     */       
/*  62 */       .put(Blocks.COPPER_TRAPDOOR, Blocks.WAXED_COPPER_TRAPDOOR)
/*  63 */       .put(Blocks.EXPOSED_COPPER_TRAPDOOR, Blocks.WAXED_EXPOSED_COPPER_TRAPDOOR)
/*  64 */       .put(Blocks.WEATHERED_COPPER_TRAPDOOR, Blocks.WAXED_WEATHERED_COPPER_TRAPDOOR)
/*  65 */       .put(Blocks.OXIDIZED_COPPER_TRAPDOOR, Blocks.WAXED_OXIDIZED_COPPER_TRAPDOOR)
/*     */       
/*  67 */       .putAll(Blocks.COPPER_BARS.waxedMapping())
/*     */       
/*  69 */       .put(Blocks.COPPER_GRATE, Blocks.WAXED_COPPER_GRATE)
/*  70 */       .put(Blocks.EXPOSED_COPPER_GRATE, Blocks.WAXED_EXPOSED_COPPER_GRATE)
/*  71 */       .put(Blocks.WEATHERED_COPPER_GRATE, Blocks.WAXED_WEATHERED_COPPER_GRATE)
/*  72 */       .put(Blocks.OXIDIZED_COPPER_GRATE, Blocks.WAXED_OXIDIZED_COPPER_GRATE)
/*     */       
/*  74 */       .put(Blocks.COPPER_BULB, Blocks.WAXED_COPPER_BULB)
/*  75 */       .put(Blocks.EXPOSED_COPPER_BULB, Blocks.WAXED_EXPOSED_COPPER_BULB)
/*  76 */       .put(Blocks.WEATHERED_COPPER_BULB, Blocks.WAXED_WEATHERED_COPPER_BULB)
/*  77 */       .put(Blocks.OXIDIZED_COPPER_BULB, Blocks.WAXED_OXIDIZED_COPPER_BULB)
/*     */       
/*  79 */       .put(Blocks.COPPER_CHEST, Blocks.WAXED_COPPER_CHEST)
/*  80 */       .put(Blocks.EXPOSED_COPPER_CHEST, Blocks.WAXED_EXPOSED_COPPER_CHEST)
/*  81 */       .put(Blocks.WEATHERED_COPPER_CHEST, Blocks.WAXED_WEATHERED_COPPER_CHEST)
/*  82 */       .put(Blocks.OXIDIZED_COPPER_CHEST, Blocks.WAXED_OXIDIZED_COPPER_CHEST)
/*     */       
/*  84 */       .put(Blocks.COPPER_GOLEM_STATUE, Blocks.WAXED_COPPER_GOLEM_STATUE)
/*  85 */       .put(Blocks.EXPOSED_COPPER_GOLEM_STATUE, Blocks.WAXED_EXPOSED_COPPER_GOLEM_STATUE)
/*  86 */       .put(Blocks.WEATHERED_COPPER_GOLEM_STATUE, Blocks.WAXED_WEATHERED_COPPER_GOLEM_STATUE)
/*  87 */       .put(Blocks.OXIDIZED_COPPER_GOLEM_STATUE, Blocks.WAXED_OXIDIZED_COPPER_GOLEM_STATUE)
/*     */       
/*  89 */       .put(Blocks.LIGHTNING_ROD, Blocks.WAXED_LIGHTNING_ROD)
/*  90 */       .put(Blocks.EXPOSED_LIGHTNING_ROD, Blocks.WAXED_EXPOSED_LIGHTNING_ROD)
/*  91 */       .put(Blocks.WEATHERED_LIGHTNING_ROD, Blocks.WAXED_WEATHERED_LIGHTNING_ROD)
/*  92 */       .put(Blocks.OXIDIZED_LIGHTNING_ROD, Blocks.WAXED_OXIDIZED_LIGHTNING_ROD)
/*     */       
/*  94 */       .putAll(Blocks.COPPER_LANTERN.waxedMapping())
/*     */       
/*  96 */       .putAll(Blocks.COPPER_CHAIN.waxedMapping())
/*  97 */       .build());
/*     */   
/*  99 */   public static final Supplier<BiMap<Block, Block>> WAX_OFF_BY_BLOCK = Suppliers.memoize(() -> ((BiMap)WAXABLES.get()).inverse());
/*     */   
/*     */   private static final String WAXED_COPPER_DOOR = "waxed_copper_door";
/*     */   
/*     */   private static final String WAXED_COPPER_TRAPDOOR = "waxed_copper_trapdoor";
/*     */   private static final String WAXED_COPPER_GOLEM_STATUE = "waxed_copper_golem_statue";
/*     */   private static final String WAXED_COPPER_CHEST = "waxed_copper_chest";
/*     */   private static final String WAXED_LIGHTNING_ROD = "waxed_lightning_rod";
/*     */   private static final String WAXED_COPPER_BAR = "waxed_copper_bar";
/*     */   private static final String WAXED_COPPER_CHAIN = "waxed_copper_chain";
/*     */   private static final String WAXED_COPPER_LANTERN = "waxed_copper_lantern";
/*     */   private static final String WAXED_COPPER_BLOCK = "waxed_copper_block";
/* 111 */   public static final ImmutableMap<Block, Pair<RecipeCategory, String>> WAXED_RECIPES = ImmutableMap.builder()
/*     */     
/* 113 */     .put(Blocks.WAXED_COPPER_BULB, Pair.of(RecipeCategory.REDSTONE, "waxed_copper_bulb"))
/* 114 */     .put(Blocks.WAXED_WEATHERED_COPPER_BULB, Pair.of(RecipeCategory.REDSTONE, "waxed_weathered_copper_bulb"))
/* 115 */     .put(Blocks.WAXED_EXPOSED_COPPER_BULB, Pair.of(RecipeCategory.REDSTONE, "waxed_exposed_copper_bulb"))
/* 116 */     .put(Blocks.WAXED_OXIDIZED_COPPER_BULB, Pair.of(RecipeCategory.REDSTONE, "waxed_oxidized_copper_bulb"))
/*     */ 
/*     */     
/* 119 */     .put(Blocks.WAXED_COPPER_DOOR, Pair.of(RecipeCategory.REDSTONE, "waxed_copper_door"))
/* 120 */     .put(Blocks.WAXED_WEATHERED_COPPER_DOOR, Pair.of(RecipeCategory.REDSTONE, "waxed_copper_door"))
/* 121 */     .put(Blocks.WAXED_EXPOSED_COPPER_DOOR, Pair.of(RecipeCategory.REDSTONE, "waxed_copper_door"))
/* 122 */     .put(Blocks.WAXED_OXIDIZED_COPPER_DOOR, Pair.of(RecipeCategory.REDSTONE, "waxed_copper_door"))
/*     */ 
/*     */     
/* 125 */     .put(Blocks.WAXED_COPPER_TRAPDOOR, Pair.of(RecipeCategory.REDSTONE, "waxed_copper_trapdoor"))
/* 126 */     .put(Blocks.WAXED_WEATHERED_COPPER_TRAPDOOR, Pair.of(RecipeCategory.REDSTONE, "waxed_copper_trapdoor"))
/* 127 */     .put(Blocks.WAXED_EXPOSED_COPPER_TRAPDOOR, Pair.of(RecipeCategory.REDSTONE, "waxed_copper_trapdoor"))
/* 128 */     .put(Blocks.WAXED_OXIDIZED_COPPER_TRAPDOOR, Pair.of(RecipeCategory.REDSTONE, "waxed_copper_trapdoor"))
/*     */ 
/*     */     
/* 131 */     .put(Blocks.WAXED_COPPER_GOLEM_STATUE, Pair.of(RecipeCategory.BUILDING_BLOCKS, "waxed_copper_golem_statue"))
/* 132 */     .put(Blocks.WAXED_WEATHERED_COPPER_GOLEM_STATUE, Pair.of(RecipeCategory.BUILDING_BLOCKS, "waxed_copper_golem_statue"))
/* 133 */     .put(Blocks.WAXED_EXPOSED_COPPER_GOLEM_STATUE, Pair.of(RecipeCategory.BUILDING_BLOCKS, "waxed_copper_golem_statue"))
/* 134 */     .put(Blocks.WAXED_OXIDIZED_COPPER_GOLEM_STATUE, Pair.of(RecipeCategory.BUILDING_BLOCKS, "waxed_copper_golem_statue"))
/*     */ 
/*     */     
/* 137 */     .put(Blocks.WAXED_COPPER_CHEST, Pair.of(RecipeCategory.BUILDING_BLOCKS, "waxed_copper_chest"))
/* 138 */     .put(Blocks.WAXED_WEATHERED_COPPER_CHEST, Pair.of(RecipeCategory.BUILDING_BLOCKS, "waxed_copper_chest"))
/* 139 */     .put(Blocks.WAXED_EXPOSED_COPPER_CHEST, Pair.of(RecipeCategory.BUILDING_BLOCKS, "waxed_copper_chest"))
/* 140 */     .put(Blocks.WAXED_OXIDIZED_COPPER_CHEST, Pair.of(RecipeCategory.BUILDING_BLOCKS, "waxed_copper_chest"))
/*     */ 
/*     */     
/* 143 */     .put(Blocks.WAXED_LIGHTNING_ROD, Pair.of(RecipeCategory.BUILDING_BLOCKS, "waxed_lightning_rod"))
/* 144 */     .put(Blocks.WAXED_WEATHERED_LIGHTNING_ROD, Pair.of(RecipeCategory.BUILDING_BLOCKS, "waxed_lightning_rod"))
/* 145 */     .put(Blocks.WAXED_EXPOSED_LIGHTNING_ROD, Pair.of(RecipeCategory.BUILDING_BLOCKS, "waxed_lightning_rod"))
/* 146 */     .put(Blocks.WAXED_OXIDIZED_LIGHTNING_ROD, Pair.of(RecipeCategory.BUILDING_BLOCKS, "waxed_lightning_rod"))
/*     */ 
/*     */     
/* 149 */     .put(Blocks.COPPER_BARS.waxed(), Pair.of(RecipeCategory.BUILDING_BLOCKS, "waxed_copper_bar"))
/* 150 */     .put(Blocks.COPPER_BARS.waxedWeathered(), Pair.of(RecipeCategory.BUILDING_BLOCKS, "waxed_copper_bar"))
/* 151 */     .put(Blocks.COPPER_BARS.waxedExposed(), Pair.of(RecipeCategory.BUILDING_BLOCKS, "waxed_copper_bar"))
/* 152 */     .put(Blocks.COPPER_BARS.waxedOxidized(), Pair.of(RecipeCategory.BUILDING_BLOCKS, "waxed_copper_bar"))
/*     */ 
/*     */     
/* 155 */     .put(Blocks.COPPER_CHAIN.waxed(), Pair.of(RecipeCategory.BUILDING_BLOCKS, "waxed_copper_chain"))
/* 156 */     .put(Blocks.COPPER_CHAIN.waxedWeathered(), Pair.of(RecipeCategory.BUILDING_BLOCKS, "waxed_copper_chain"))
/* 157 */     .put(Blocks.COPPER_CHAIN.waxedExposed(), Pair.of(RecipeCategory.BUILDING_BLOCKS, "waxed_copper_chain"))
/* 158 */     .put(Blocks.COPPER_CHAIN.waxedOxidized(), Pair.of(RecipeCategory.BUILDING_BLOCKS, "waxed_copper_chain"))
/*     */ 
/*     */     
/* 161 */     .put(Blocks.COPPER_LANTERN.waxed(), Pair.of(RecipeCategory.BUILDING_BLOCKS, "waxed_copper_lantern"))
/* 162 */     .put(Blocks.COPPER_LANTERN.waxedWeathered(), Pair.of(RecipeCategory.BUILDING_BLOCKS, "waxed_copper_lantern"))
/* 163 */     .put(Blocks.COPPER_LANTERN.waxedExposed(), Pair.of(RecipeCategory.BUILDING_BLOCKS, "waxed_copper_lantern"))
/* 164 */     .put(Blocks.COPPER_LANTERN.waxedOxidized(), Pair.of(RecipeCategory.BUILDING_BLOCKS, "waxed_copper_lantern"))
/*     */ 
/*     */     
/* 167 */     .put(Blocks.WAXED_COPPER_BLOCK, Pair.of(RecipeCategory.BUILDING_BLOCKS, "waxed_copper_block"))
/* 168 */     .put(Blocks.WAXED_WEATHERED_COPPER, Pair.of(RecipeCategory.BUILDING_BLOCKS, "waxed_copper_block"))
/* 169 */     .put(Blocks.WAXED_EXPOSED_COPPER, Pair.of(RecipeCategory.BUILDING_BLOCKS, "waxed_copper_block"))
/* 170 */     .put(Blocks.WAXED_OXIDIZED_COPPER, Pair.of(RecipeCategory.BUILDING_BLOCKS, "waxed_copper_block"))
/* 171 */     .build();
/*     */ 
/*     */   
/* 174 */   public HoneycombItem(Item.Properties properties) { super(properties); }
/*     */ 
/*     */ 
/*     */   
/*     */   public InteractionResult useOn(UseOnContext context) {
/* 179 */     Level level = context.getLevel();
/* 180 */     BlockPos pos = context.getClickedPos();
/* 181 */     BlockState oldState = level.getBlockState(pos);
/*     */     
/* 183 */     return (InteractionResult)getWaxed(oldState).map(waxedState -> {
/* 184 */           Player player = context.getPlayer();
/* 185 */           ItemStack itemInHand = context.getItemInHand();
/*     */           
/* 187 */           if (player instanceof ServerPlayer) { ServerPlayer serverPlayer = (ServerPlayer)player;
/* 188 */             CriteriaTriggers.ITEM_USED_ON_BLOCK.trigger(serverPlayer, pos, itemInHand); }
/*     */           
/* 190 */           itemInHand.shrink(1);
/* 191 */           level.setBlock(pos, waxedState, 11);
/* 192 */           level.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(player, waxedState));
/* 193 */           level.levelEvent(player, 3003, pos, 0);
/*     */           
/* 195 */           if (oldState.getBlock() instanceof ChestBlock && oldState.getValue(ChestBlock.TYPE) != ChestType.SINGLE) {
/* 196 */             BlockPos neighborPos = ChestBlock.getConnectedBlockPos(pos, oldState);
/* 197 */             level.gameEvent(GameEvent.BLOCK_CHANGE, neighborPos, GameEvent.Context.of(player, level.getBlockState(neighborPos)));
/* 198 */             level.levelEvent(player, 3003, neighborPos, 0);
/*     */           } 
/*     */           
/* 201 */           return InteractionResult.SUCCESS;
/* 202 */         }).orElse(InteractionResult.PASS);
/*     */   }
/*     */ 
/*     */   
/* 206 */   public static Optional<BlockState> getWaxed(BlockState oldState) { return Optional.ofNullable((Block)((BiMap)WAXABLES.get()).get(oldState.getBlock())).map(b -> b.withPropertiesOf(oldState)); }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean tryApplyToSign(Level level, SignBlockEntity sign, boolean isFrontText, Player player) {
/* 211 */     if (sign.setWaxed(true)) {
/* 212 */       level.levelEvent(null, 3003, sign.getBlockPos(), 0);
/* 213 */       return true;
/*     */     } 
/* 215 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 220 */   public boolean canApplyToSign(SignText text, Player player) { return true; }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\item\HoneycombItem.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */