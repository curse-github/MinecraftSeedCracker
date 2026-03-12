/*     */ package net.minecraft.core.dispenser;
/*     */ 
/*     */ import com.mojang.logging.LogUtils;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import java.util.function.Consumer;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.tags.FluidTags;
/*     */ import net.minecraft.world.entity.EntitySelector;
/*     */ import net.minecraft.world.entity.EntitySpawnReason;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.SlotAccess;
/*     */ import net.minecraft.world.entity.animal.armadillo.Armadillo;
/*     */ import net.minecraft.world.entity.animal.equine.AbstractChestedHorse;
/*     */ import net.minecraft.world.entity.decoration.ArmorStand;
/*     */ import net.minecraft.world.entity.item.PrimedTnt;
/*     */ import net.minecraft.world.item.BoneMealItem;
/*     */ import net.minecraft.world.item.DispensibleContainerItem;
/*     */ import net.minecraft.world.item.DyeColor;
/*     */ import net.minecraft.world.item.HoneycombItem;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.item.SpawnEggItem;
/*     */ import net.minecraft.world.item.alchemy.PotionContents;
/*     */ import net.minecraft.world.item.alchemy.Potions;
/*     */ import net.minecraft.world.level.block.BaseFireBlock;
/*     */ import net.minecraft.world.level.block.BeehiveBlock;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.BucketPickup;
/*     */ import net.minecraft.world.level.block.CampfireBlock;
/*     */ import net.minecraft.world.level.block.CandleBlock;
/*     */ import net.minecraft.world.level.block.CandleCakeBlock;
/*     */ import net.minecraft.world.level.block.CarvedPumpkinBlock;
/*     */ import net.minecraft.world.level.block.DispenserBlock;
/*     */ import net.minecraft.world.level.block.RespawnAnchorBlock;
/*     */ import net.minecraft.world.level.block.ShulkerBoxBlock;
/*     */ import net.minecraft.world.level.block.SkullBlock;
/*     */ import net.minecraft.world.level.block.TntBlock;
/*     */ import net.minecraft.world.level.block.WitherSkullBlock;
/*     */ import net.minecraft.world.level.block.entity.BeehiveBlockEntity;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.entity.SkullBlockEntity;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.RotationSegment;
/*     */ import net.minecraft.world.level.gameevent.GameEvent;
/*     */ import net.minecraft.world.level.gamerules.GameRules;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ 
/*     */ 
/*     */ public interface DispenseItemBehavior
/*     */ {
/*  65 */   public static final Logger LOGGER = LogUtils.getLogger();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  70 */   public static final DispenseItemBehavior NOOP = (source, dispensed) -> dispensed;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static void bootStrap() {
/*  80 */     DispenserBlock.registerProjectileBehavior(Items.ARROW);
/*  81 */     DispenserBlock.registerProjectileBehavior(Items.TIPPED_ARROW);
/*  82 */     DispenserBlock.registerProjectileBehavior(Items.SPECTRAL_ARROW);
/*  83 */     DispenserBlock.registerProjectileBehavior(Items.EGG);
/*  84 */     DispenserBlock.registerProjectileBehavior(Items.BLUE_EGG);
/*  85 */     DispenserBlock.registerProjectileBehavior(Items.BROWN_EGG);
/*  86 */     DispenserBlock.registerProjectileBehavior(Items.SNOWBALL);
/*  87 */     DispenserBlock.registerProjectileBehavior(Items.EXPERIENCE_BOTTLE);
/*  88 */     DispenserBlock.registerProjectileBehavior(Items.SPLASH_POTION);
/*  89 */     DispenserBlock.registerProjectileBehavior(Items.LINGERING_POTION);
/*  90 */     DispenserBlock.registerProjectileBehavior(Items.FIREWORK_ROCKET);
/*  91 */     DispenserBlock.registerProjectileBehavior(Items.FIRE_CHARGE);
/*  92 */     DispenserBlock.registerProjectileBehavior(Items.WIND_CHARGE);
/*     */     
/*  94 */     spawnEggBehavior = new DefaultDispenseItemBehavior()
/*     */       {
/*     */         public ItemStack execute(BlockSource source, ItemStack dispensed) {
/*  97 */           Direction direction = (Direction)source.state().getValue(DispenserBlock.FACING);
/*     */           
/*  99 */           EntityType<?> type = ((SpawnEggItem)dispensed.getItem()).getType(dispensed);
/* 100 */           if (type == null) {
/* 101 */             return dispensed;
/*     */           }
/*     */           try {
/* 104 */             type.spawn(source.level(), dispensed, null, source.pos().relative(direction), EntitySpawnReason.DISPENSER, (direction != Direction.UP), false);
/* 105 */           } catch (Exception e) {
/* 106 */             LOGGER.error("Error while dispensing spawn egg from dispenser at {}", source.pos(), e);
/* 107 */             return ItemStack.EMPTY;
/*     */           } 
/* 109 */           dispensed.shrink(1);
/* 110 */           source.level().gameEvent(null, GameEvent.ENTITY_PLACE, source.pos());
/* 111 */           return dispensed;
/*     */         }
/*     */       };
/*     */     
/* 115 */     for (SpawnEggItem item : SpawnEggItem.eggs()) {
/* 116 */       DispenserBlock.registerBehavior(item, spawnEggBehavior);
/*     */     }
/*     */     
/* 119 */     DispenserBlock.registerBehavior(Items.ARMOR_STAND, new DefaultDispenseItemBehavior()
/*     */         {
/*     */           public ItemStack execute(BlockSource source, ItemStack dispensed) {
/* 122 */             Direction direction = (Direction)source.state().getValue(DispenserBlock.FACING);
/* 123 */             BlockPos pos = source.pos().relative(direction);
/* 124 */             ServerLevel serverLevel = source.level();
/* 125 */             Consumer<ArmorStand> postSpawnConfig = EntityType.appendDefaultStackConfig(armorStand -> armorStand.setYRot(direction.toYRot()), serverLevel, dispensed, null);
/* 126 */             ArmorStand armorStand = (ArmorStand)EntityType.ARMOR_STAND.spawn(serverLevel, postSpawnConfig, pos, EntitySpawnReason.DISPENSER, false, false);
/* 127 */             if (armorStand != null) {
/* 128 */               dispensed.shrink(1);
/*     */             }
/* 130 */             return dispensed;
/*     */           }
/*     */         });
/*     */     
/* 134 */     DispenserBlock.registerBehavior(Items.CHEST, new OptionalDispenseItemBehavior()
/*     */         {
/*     */           public ItemStack execute(BlockSource source, ItemStack dispensed) {
/* 137 */             BlockPos pos = source.pos().relative((Direction)source.state().getValue(DispenserBlock.FACING));
/* 138 */             List<AbstractChestedHorse> entities = source.level().getEntitiesOfClass(AbstractChestedHorse.class, new AABB(pos), entity -> (entity.isAlive() && !entity.hasChest()));
/*     */             
/* 140 */             for (AbstractChestedHorse abstractChestedHorse : entities) {
/* 141 */               if (abstractChestedHorse.isTamed()) {
/* 142 */                 SlotAccess slot = abstractChestedHorse.getSlot(499);
/* 143 */                 if (slot != null && slot.set(dispensed)) {
/* 144 */                   dispensed.shrink(1);
/* 145 */                   setSuccess(true);
/* 146 */                   return dispensed;
/*     */                 } 
/*     */               } 
/*     */             } 
/*     */             
/* 151 */             return super.execute(source, dispensed);
/*     */           }
/*     */         });
/*     */     
/* 155 */     DispenserBlock.registerBehavior(Items.OAK_BOAT, new BoatDispenseItemBehavior(EntityType.OAK_BOAT));
/* 156 */     DispenserBlock.registerBehavior(Items.SPRUCE_BOAT, new BoatDispenseItemBehavior(EntityType.SPRUCE_BOAT));
/* 157 */     DispenserBlock.registerBehavior(Items.BIRCH_BOAT, new BoatDispenseItemBehavior(EntityType.BIRCH_BOAT));
/* 158 */     DispenserBlock.registerBehavior(Items.JUNGLE_BOAT, new BoatDispenseItemBehavior(EntityType.JUNGLE_BOAT));
/* 159 */     DispenserBlock.registerBehavior(Items.DARK_OAK_BOAT, new BoatDispenseItemBehavior(EntityType.DARK_OAK_BOAT));
/* 160 */     DispenserBlock.registerBehavior(Items.ACACIA_BOAT, new BoatDispenseItemBehavior(EntityType.ACACIA_BOAT));
/* 161 */     DispenserBlock.registerBehavior(Items.CHERRY_BOAT, new BoatDispenseItemBehavior(EntityType.CHERRY_BOAT));
/* 162 */     DispenserBlock.registerBehavior(Items.MANGROVE_BOAT, new BoatDispenseItemBehavior(EntityType.MANGROVE_BOAT));
/* 163 */     DispenserBlock.registerBehavior(Items.PALE_OAK_BOAT, new BoatDispenseItemBehavior(EntityType.PALE_OAK_BOAT));
/* 164 */     DispenserBlock.registerBehavior(Items.BAMBOO_RAFT, new BoatDispenseItemBehavior(EntityType.BAMBOO_RAFT));
/*     */     
/* 166 */     DispenserBlock.registerBehavior(Items.OAK_CHEST_BOAT, new BoatDispenseItemBehavior(EntityType.OAK_CHEST_BOAT));
/* 167 */     DispenserBlock.registerBehavior(Items.SPRUCE_CHEST_BOAT, new BoatDispenseItemBehavior(EntityType.SPRUCE_CHEST_BOAT));
/* 168 */     DispenserBlock.registerBehavior(Items.BIRCH_CHEST_BOAT, new BoatDispenseItemBehavior(EntityType.BIRCH_CHEST_BOAT));
/* 169 */     DispenserBlock.registerBehavior(Items.JUNGLE_CHEST_BOAT, new BoatDispenseItemBehavior(EntityType.JUNGLE_CHEST_BOAT));
/* 170 */     DispenserBlock.registerBehavior(Items.DARK_OAK_CHEST_BOAT, new BoatDispenseItemBehavior(EntityType.DARK_OAK_CHEST_BOAT));
/* 171 */     DispenserBlock.registerBehavior(Items.ACACIA_CHEST_BOAT, new BoatDispenseItemBehavior(EntityType.ACACIA_CHEST_BOAT));
/* 172 */     DispenserBlock.registerBehavior(Items.CHERRY_CHEST_BOAT, new BoatDispenseItemBehavior(EntityType.CHERRY_CHEST_BOAT));
/* 173 */     DispenserBlock.registerBehavior(Items.MANGROVE_CHEST_BOAT, new BoatDispenseItemBehavior(EntityType.MANGROVE_CHEST_BOAT));
/* 174 */     DispenserBlock.registerBehavior(Items.PALE_OAK_CHEST_BOAT, new BoatDispenseItemBehavior(EntityType.PALE_OAK_CHEST_BOAT));
/* 175 */     DispenserBlock.registerBehavior(Items.BAMBOO_CHEST_RAFT, new BoatDispenseItemBehavior(EntityType.BAMBOO_CHEST_RAFT));
/*     */     
/* 177 */     DispenseItemBehavior filledBucketBehavior = new DefaultDispenseItemBehavior() {
/* 178 */         private final DefaultDispenseItemBehavior defaultDispenseItemBehavior = new DefaultDispenseItemBehavior();
/*     */ 
/*     */         
/*     */         public ItemStack execute(BlockSource source, ItemStack dispensed) {
/* 182 */           DispensibleContainerItem bucket = (DispensibleContainerItem)dispensed.getItem();
/* 183 */           BlockPos target = source.pos().relative((Direction)source.state().getValue(DispenserBlock.FACING));
/*     */           
/* 185 */           ServerLevel serverLevel = source.level();
/* 186 */           if (bucket.emptyContents(null, serverLevel, target, null)) {
/* 187 */             bucket.checkExtraContent(null, serverLevel, dispensed, target);
/* 188 */             return consumeWithRemainder(source, dispensed, new ItemStack(Items.BUCKET));
/*     */           } 
/*     */           
/* 191 */           return this.defaultDispenseItemBehavior.dispense(source, dispensed);
/*     */         }
/*     */       };
/* 194 */     DispenserBlock.registerBehavior(Items.LAVA_BUCKET, filledBucketBehavior);
/* 195 */     DispenserBlock.registerBehavior(Items.WATER_BUCKET, filledBucketBehavior);
/* 196 */     DispenserBlock.registerBehavior(Items.POWDER_SNOW_BUCKET, filledBucketBehavior);
/* 197 */     DispenserBlock.registerBehavior(Items.SALMON_BUCKET, filledBucketBehavior);
/* 198 */     DispenserBlock.registerBehavior(Items.COD_BUCKET, filledBucketBehavior);
/* 199 */     DispenserBlock.registerBehavior(Items.PUFFERFISH_BUCKET, filledBucketBehavior);
/* 200 */     DispenserBlock.registerBehavior(Items.TROPICAL_FISH_BUCKET, filledBucketBehavior);
/* 201 */     DispenserBlock.registerBehavior(Items.AXOLOTL_BUCKET, filledBucketBehavior);
/* 202 */     DispenserBlock.registerBehavior(Items.TADPOLE_BUCKET, filledBucketBehavior);
/*     */     
/* 204 */     DispenserBlock.registerBehavior(Items.BUCKET, new DefaultDispenseItemBehavior() {
/*     */           public ItemStack execute(BlockSource source, ItemStack dispensed) {
/*     */             Item targetType;
/* 207 */             ServerLevel serverLevel = source.level();
/*     */             
/* 209 */             BlockPos target = source.pos().relative((Direction)source.state().getValue(DispenserBlock.FACING));
/*     */             
/* 211 */             BlockState blockState = serverLevel.getBlockState(target);
/* 212 */             Block block = blockState.getBlock();
/*     */ 
/*     */ 
/*     */             
/* 216 */             if (block instanceof BucketPickup) { BucketPickup bucket = (BucketPickup)block;
/* 217 */               ItemStack pickup = bucket.pickupBlock(null, serverLevel, target, blockState);
/* 218 */               if (pickup.isEmpty()) {
/* 219 */                 return super.execute(source, dispensed);
/*     */               }
/* 221 */               serverLevel.gameEvent(null, GameEvent.FLUID_PICKUP, target);
/* 222 */               targetType = pickup.getItem(); }
/*     */             else
/* 224 */             { return super.execute(source, dispensed); }
/*     */ 
/*     */             
/* 227 */             return consumeWithRemainder(source, dispensed, new ItemStack(targetType));
/*     */           }
/*     */         });
/*     */     
/* 231 */     DispenserBlock.registerBehavior(Items.FLINT_AND_STEEL, new OptionalDispenseItemBehavior()
/*     */         {
/*     */           protected ItemStack execute(BlockSource source, ItemStack dispensed) {
/* 234 */             ServerLevel level = source.level();
/*     */             
/* 236 */             setSuccess(true);
/*     */             
/* 238 */             Direction facing = (Direction)source.state().getValue(DispenserBlock.FACING);
/* 239 */             BlockPos targetPos = source.pos().relative(facing);
/* 240 */             BlockState target = level.getBlockState(targetPos);
/* 241 */             if (BaseFireBlock.canBePlacedAt(level, targetPos, facing)) {
/* 242 */               level.setBlockAndUpdate(targetPos, BaseFireBlock.getState(level, targetPos));
/* 243 */               level.gameEvent(null, GameEvent.BLOCK_PLACE, targetPos);
/* 244 */             } else if (CampfireBlock.canLight(target) || CandleBlock.canLight(target) || CandleCakeBlock.canLight(target)) {
/* 245 */               level.setBlockAndUpdate(targetPos, (BlockState)target.setValue(BlockStateProperties.LIT, Boolean.valueOf(true)));
/* 246 */               level.gameEvent(null, GameEvent.BLOCK_CHANGE, targetPos);
/* 247 */             } else if (target.getBlock() instanceof TntBlock) {
/* 248 */               if (TntBlock.prime(level, targetPos)) {
/* 249 */                 level.removeBlock(targetPos, false);
/*     */               } else {
/* 251 */                 setSuccess(false);
/*     */               } 
/*     */             } else {
/* 254 */               setSuccess(false);
/*     */             } 
/*     */             
/* 257 */             if (isSuccess())
/* 258 */               dispensed.hurtAndBreak(1, level, null, item -> {
/*     */                   
/*     */                   }); 
/* 261 */             return dispensed;
/*     */           }
/*     */         });
/*     */     
/* 265 */     DispenserBlock.registerBehavior(Items.BONE_MEAL, new OptionalDispenseItemBehavior()
/*     */         {
/*     */           protected ItemStack execute(BlockSource source, ItemStack dispensed) {
/* 268 */             setSuccess(true);
/* 269 */             ServerLevel serverLevel = source.level();
/*     */             
/* 271 */             BlockPos target = source.pos().relative((Direction)source.state().getValue(DispenserBlock.FACING));
/* 272 */             if (BoneMealItem.growCrop(dispensed, serverLevel, target) || BoneMealItem.growWaterPlant(dispensed, serverLevel, target, null)) {
/* 273 */               if (!serverLevel.isClientSide()) {
/* 274 */                 serverLevel.levelEvent(1505, target, 15);
/*     */               }
/*     */             } else {
/* 277 */               setSuccess(false);
/*     */             } 
/*     */             
/* 280 */             return dispensed;
/*     */           }
/*     */         });
/*     */     
/* 284 */     DispenserBlock.registerBehavior(Blocks.TNT, new OptionalDispenseItemBehavior()
/*     */         {
/*     */           protected ItemStack execute(BlockSource source, ItemStack dispensed) {
/* 287 */             ServerLevel level = source.level();
/* 288 */             if (!((Boolean)level.getGameRules().get(GameRules.TNT_EXPLODES)).booleanValue()) {
/* 289 */               setSuccess(false);
/* 290 */               return dispensed;
/*     */             } 
/*     */             
/* 293 */             BlockPos target = source.pos().relative((Direction)source.state().getValue(DispenserBlock.FACING));
/*     */             
/* 295 */             PrimedTnt tnt = new PrimedTnt(level, target.getX() + 0.5D, target.getY(), target.getZ() + 0.5D, null);
/* 296 */             level.addFreshEntity(tnt);
/* 297 */             level.playSound(null, tnt.getX(), tnt.getY(), tnt.getZ(), SoundEvents.TNT_PRIMED, SoundSource.BLOCKS, 1.0F, 1.0F);
/* 298 */             level.gameEvent(null, GameEvent.ENTITY_PLACE, target);
/*     */             
/* 300 */             dispensed.shrink(1);
/* 301 */             setSuccess(true);
/* 302 */             return dispensed;
/*     */           }
/*     */         });
/*     */     
/* 306 */     DispenserBlock.registerBehavior(Items.WITHER_SKELETON_SKULL, new OptionalDispenseItemBehavior()
/*     */         {
/*     */           protected ItemStack execute(BlockSource source, ItemStack dispensed) {
/* 309 */             ServerLevel serverLevel = source.level();
/* 310 */             Direction direction = (Direction)source.state().getValue(DispenserBlock.FACING);
/* 311 */             BlockPos target = source.pos().relative(direction);
/*     */             
/* 313 */             if (serverLevel.isEmptyBlock(target) && WitherSkullBlock.canSpawnMob(serverLevel, target, dispensed)) {
/* 314 */               serverLevel.setBlock(target, (BlockState)Blocks.WITHER_SKELETON_SKULL.defaultBlockState().setValue(SkullBlock.ROTATION, Integer.valueOf(RotationSegment.convertToSegment(direction))), 3);
/* 315 */               serverLevel.gameEvent(null, GameEvent.BLOCK_PLACE, target);
/* 316 */               BlockEntity skull = serverLevel.getBlockEntity(target);
/* 317 */               if (skull instanceof SkullBlockEntity) {
/* 318 */                 WitherSkullBlock.checkSpawn(serverLevel, target, (SkullBlockEntity)skull);
/*     */               }
/* 320 */               dispensed.shrink(1);
/* 321 */               setSuccess(true);
/*     */             } else {
/* 323 */               setSuccess(EquipmentDispenseItemBehavior.dispenseEquipment(source, dispensed));
/*     */             } 
/* 325 */             return dispensed;
/*     */           }
/*     */         });
/*     */     
/* 329 */     DispenserBlock.registerBehavior(Blocks.CARVED_PUMPKIN, new OptionalDispenseItemBehavior()
/*     */         {
/*     */           protected ItemStack execute(BlockSource source, ItemStack dispensed) {
/* 332 */             ServerLevel serverLevel = source.level();
/* 333 */             BlockPos target = source.pos().relative((Direction)source.state().getValue(DispenserBlock.FACING));
/* 334 */             CarvedPumpkinBlock pumpkinBlock = (CarvedPumpkinBlock)Blocks.CARVED_PUMPKIN;
/*     */             
/* 336 */             if (serverLevel.isEmptyBlock(target) && pumpkinBlock.canSpawnGolem(serverLevel, target)) {
/* 337 */               if (!serverLevel.isClientSide()) {
/* 338 */                 serverLevel.setBlock(target, pumpkinBlock.defaultBlockState(), 3);
/* 339 */                 serverLevel.gameEvent(null, GameEvent.BLOCK_PLACE, target);
/*     */               } 
/* 341 */               dispensed.shrink(1);
/* 342 */               setSuccess(true);
/*     */             } else {
/* 344 */               setSuccess(EquipmentDispenseItemBehavior.dispenseEquipment(source, dispensed));
/*     */             } 
/* 346 */             return dispensed;
/*     */           }
/*     */         });
/*     */     
/* 350 */     DispenserBlock.registerBehavior(Blocks.SHULKER_BOX.asItem(), new ShulkerBoxDispenseBehavior());
/* 351 */     for (DyeColor color : DyeColor.values()) {
/* 352 */       DispenserBlock.registerBehavior(ShulkerBoxBlock.getBlockByColor(color).asItem(), new ShulkerBoxDispenseBehavior());
/*     */     }
/*     */     
/* 355 */     DispenserBlock.registerBehavior(Items.GLASS_BOTTLE.asItem(), new OptionalDispenseItemBehavior() {
/*     */           private ItemStack takeLiquid(BlockSource source, ItemStack dispensed, ItemStack filledItemStack) {
/* 357 */             source.level().gameEvent(null, GameEvent.FLUID_PICKUP, source.pos());
/* 358 */             return consumeWithRemainder(source, dispensed, filledItemStack);
/*     */           }
/*     */ 
/*     */           
/*     */           public ItemStack execute(BlockSource source, ItemStack dispensed) {
/* 363 */             setSuccess(false);
/* 364 */             ServerLevel level = source.level();
/*     */             
/* 366 */             BlockPos target = source.pos().relative((Direction)source.state().getValue(DispenserBlock.FACING));
/*     */             
/* 368 */             BlockState state = level.getBlockState(target);
/*     */             
/* 370 */             if (state.is(BlockTags.BEEHIVES, s -> (s.hasProperty(BeehiveBlock.HONEY_LEVEL) && s.getBlock() instanceof BeehiveBlock)) && ((Integer)state.getValue(BeehiveBlock.HONEY_LEVEL)).intValue() >= 5) {
/* 371 */               ((BeehiveBlock)state.getBlock()).releaseBeesAndResetHoneyLevel(level, state, target, null, BeehiveBlockEntity.BeeReleaseStatus.BEE_RELEASED);
/* 372 */               setSuccess(true);
/* 373 */               return takeLiquid(source, dispensed, new ItemStack(Items.HONEY_BOTTLE));
/* 374 */             }  if (level.getFluidState(target).is(FluidTags.WATER)) {
/* 375 */               setSuccess(true);
/* 376 */               return takeLiquid(source, dispensed, PotionContents.createItemStack(Items.POTION, Potions.WATER));
/*     */             } 
/* 378 */             return super.execute(source, dispensed);
/*     */           }
/*     */         });
/*     */ 
/*     */     
/* 383 */     DispenserBlock.registerBehavior(Items.GLOWSTONE, new OptionalDispenseItemBehavior()
/*     */         {
/*     */           public ItemStack execute(BlockSource source, ItemStack dispensed) {
/* 386 */             Direction direction = (Direction)source.state().getValue(DispenserBlock.FACING);
/* 387 */             BlockPos pos = source.pos().relative(direction);
/* 388 */             ServerLevel serverLevel = source.level();
/* 389 */             BlockState blockState = serverLevel.getBlockState(pos);
/* 390 */             setSuccess(true);
/* 391 */             if (blockState.is(Blocks.RESPAWN_ANCHOR)) {
/* 392 */               if (((Integer)blockState.getValue(RespawnAnchorBlock.CHARGE)).intValue() != 4) {
/* 393 */                 RespawnAnchorBlock.charge(null, serverLevel, pos, blockState);
/* 394 */                 dispensed.shrink(1);
/*     */               } else {
/* 396 */                 setSuccess(false);
/*     */               } 
/*     */               
/* 399 */               return dispensed;
/*     */             } 
/* 401 */             return super.execute(source, dispensed);
/*     */           }
/*     */         });
/*     */ 
/*     */     
/* 406 */     DispenserBlock.registerBehavior(Items.SHEARS.asItem(), new ShearsDispenseItemBehavior());
/*     */     
/* 408 */     DispenserBlock.registerBehavior(Items.BRUSH.asItem(), new OptionalDispenseItemBehavior()
/*     */         {
/*     */           protected ItemStack execute(BlockSource source, ItemStack dispensed) {
/* 411 */             ServerLevel level = source.level();
/*     */             
/* 413 */             BlockPos pos = source.pos().relative((Direction)source.state().getValue(DispenserBlock.FACING));
/*     */             
/* 415 */             List<Armadillo> armadillos = level.getEntitiesOfClass(Armadillo.class, new AABB(pos), EntitySelector.NO_SPECTATORS);
/* 416 */             if (armadillos.isEmpty()) {
/* 417 */               setSuccess(false);
/* 418 */               return dispensed;
/*     */             } 
/* 420 */             for (Armadillo armadillo : armadillos) {
/* 421 */               if (armadillo.brushOffScute(null, dispensed)) {
/* 422 */                 dispensed.hurtAndBreak(16, level, null, item -> { 
/* 423 */                     }); return dispensed;
/*     */               } 
/*     */             } 
/* 426 */             setSuccess(false);
/* 427 */             return dispensed;
/*     */           }
/*     */         });
/*     */     
/* 431 */     DispenserBlock.registerBehavior(Items.HONEYCOMB, new OptionalDispenseItemBehavior()
/*     */         {
/*     */           public ItemStack execute(BlockSource source, ItemStack dispensed) {
/* 434 */             BlockPos pos = source.pos().relative((Direction)source.state().getValue(DispenserBlock.FACING));
/* 435 */             ServerLevel serverLevel = source.level();
/* 436 */             BlockState blockState = serverLevel.getBlockState(pos);
/*     */             
/* 438 */             Optional<BlockState> maybeWaxed = HoneycombItem.getWaxed(blockState);
/* 439 */             if (maybeWaxed.isPresent()) {
/* 440 */               serverLevel.setBlockAndUpdate(pos, (BlockState)maybeWaxed.get());
/* 441 */               serverLevel.levelEvent(3003, pos, 0);
/* 442 */               dispensed.shrink(1);
/* 443 */               setSuccess(true);
/*     */               
/* 445 */               return dispensed;
/*     */             } 
/* 447 */             return super.execute(source, dispensed);
/*     */           }
/*     */         });
/*     */     
/* 451 */     DispenserBlock.registerBehavior(Items.POTION, new DefaultDispenseItemBehavior() {
/* 452 */           private final DefaultDispenseItemBehavior defaultDispenseItemBehavior = new DefaultDispenseItemBehavior();
/*     */ 
/*     */           
/*     */           public ItemStack execute(BlockSource source, ItemStack dispensed) {
/* 456 */             PotionContents potion = (PotionContents)dispensed.getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY);
/* 457 */             if (!potion.is(Potions.WATER)) {
/* 458 */               return this.defaultDispenseItemBehavior.dispense(source, dispensed);
/*     */             }
/*     */             
/* 461 */             ServerLevel level = source.level();
/* 462 */             BlockPos pos = source.pos();
/*     */             
/* 464 */             BlockPos target = source.pos().relative((Direction)source.state().getValue(DispenserBlock.FACING));
/* 465 */             if (level.getBlockState(target).is(BlockTags.CONVERTABLE_TO_MUD)) {
/* 466 */               if (!level.isClientSide()) {
/* 467 */                 for (int i = 0; i < 5; i++) {
/* 468 */                   level.sendParticles(ParticleTypes.SPLASH, pos.getX() + level.random.nextDouble(), (pos.getY() + 1), pos.getZ() + level.random.nextDouble(), 1, 0.0D, 0.0D, 0.0D, 1.0D);
/*     */                 }
/*     */               }
/*     */               
/* 472 */               level.playSound(null, pos, SoundEvents.BOTTLE_EMPTY, SoundSource.BLOCKS, 1.0F, 1.0F);
/* 473 */               level.gameEvent(null, GameEvent.FLUID_PLACE, pos);
/*     */               
/* 475 */               level.setBlockAndUpdate(target, Blocks.MUD.defaultBlockState());
/*     */               
/* 477 */               return consumeWithRemainder(source, dispensed, new ItemStack(Items.GLASS_BOTTLE));
/*     */             } 
/*     */             
/* 480 */             return this.defaultDispenseItemBehavior.dispense(source, dispensed);
/*     */           }
/*     */         });
/*     */     
/* 484 */     DispenserBlock.registerBehavior(Items.MINECART, new MinecartDispenseItemBehavior(EntityType.MINECART));
/* 485 */     DispenserBlock.registerBehavior(Items.CHEST_MINECART, new MinecartDispenseItemBehavior(EntityType.CHEST_MINECART));
/* 486 */     DispenserBlock.registerBehavior(Items.FURNACE_MINECART, new MinecartDispenseItemBehavior(EntityType.FURNACE_MINECART));
/* 487 */     DispenserBlock.registerBehavior(Items.TNT_MINECART, new MinecartDispenseItemBehavior(EntityType.TNT_MINECART));
/* 488 */     DispenserBlock.registerBehavior(Items.HOPPER_MINECART, new MinecartDispenseItemBehavior(EntityType.HOPPER_MINECART));
/* 489 */     DispenserBlock.registerBehavior(Items.COMMAND_BLOCK_MINECART, new MinecartDispenseItemBehavior(EntityType.COMMAND_BLOCK_MINECART));
/*     */   }
/*     */   
/*     */   ItemStack dispense(BlockSource paramBlockSource, ItemStack paramItemStack);
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\core\dispenser\DispenseItemBehavior.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */