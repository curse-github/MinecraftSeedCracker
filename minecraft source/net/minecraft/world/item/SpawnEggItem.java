/*     */ package net.minecraft.world.item;
/*     */ 
/*     */ import com.google.common.collect.Iterables;
/*     */ import com.google.common.collect.Maps;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.server.permissions.Permissions;
/*     */ import net.minecraft.stats.Stats;
/*     */ import net.minecraft.world.Difficulty;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.entity.AgeableMob;
/*     */ import net.minecraft.world.entity.EntitySpawnReason;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.flag.FeatureFlagSet;
/*     */ import net.minecraft.world.item.component.TypedEntityData;
/*     */ import net.minecraft.world.item.context.UseOnContext;
/*     */ import net.minecraft.world.level.ClipContext;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.Spawner;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.gameevent.GameEvent;
/*     */ import net.minecraft.world.phys.BlockHitResult;
/*     */ import net.minecraft.world.phys.HitResult;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ 
/*     */ public class SpawnEggItem
/*     */   extends Item
/*     */ {
/*  42 */   private static final Map<EntityType<?>, SpawnEggItem> BY_ID = Maps.newIdentityHashMap();
/*     */   
/*     */   public SpawnEggItem(Item.Properties properties) {
/*  45 */     super(properties);
/*     */     
/*  47 */     TypedEntityData<EntityType<?>> entityType = (TypedEntityData)components().get(DataComponents.ENTITY_DATA);
/*  48 */     if (entityType != null)
/*  49 */       BY_ID.put((EntityType)entityType.type(), this); 
/*     */   }
/*     */   
/*     */   public InteractionResult useOn(UseOnContext context) {
/*     */     BlockPos spawnPos;
/*     */     ServerLevel serverLevel;
/*  55 */     Level level = context.getLevel();
/*  56 */     if (level instanceof ServerLevel) { serverLevel = (ServerLevel)level; }
/*  57 */     else { return InteractionResult.SUCCESS; }
/*     */ 
/*     */     
/*  60 */     ItemStack itemStack = context.getItemInHand();
/*  61 */     BlockPos pos = context.getClickedPos();
/*  62 */     Direction clickedFace = context.getClickedFace();
/*     */     
/*  64 */     BlockState blockState = level.getBlockState(pos);
/*  65 */     BlockEntity blockEntity = level.getBlockEntity(pos); if (blockEntity instanceof Spawner) { spawnPos = (Spawner)blockEntity;
/*  66 */       EntityType<?> type = getType(itemStack);
/*     */       
/*  68 */       if (type == null) {
/*  69 */         return InteractionResult.FAIL;
/*     */       }
/*     */       
/*  72 */       if (!serverLevel.isSpawnerBlockEnabled()) {
/*  73 */         Player player = context.getPlayer(); if (player instanceof ServerPlayer) { ServerPlayer serverPlayer = (ServerPlayer)player;
/*  74 */           serverPlayer.sendSystemMessage(Component.translatable("advMode.notEnabled.spawner")); }
/*     */         
/*  76 */         return InteractionResult.FAIL;
/*     */       } 
/*     */       
/*  79 */       spawnPos.setEntityId(type, level.getRandom());
/*  80 */       level.sendBlockUpdated(pos, blockState, blockState, 3);
/*  81 */       level.gameEvent(context.getPlayer(), GameEvent.BLOCK_CHANGE, pos);
/*  82 */       itemStack.shrink(1);
/*  83 */       return InteractionResult.SUCCESS; }
/*     */ 
/*     */ 
/*     */     
/*  87 */     if (blockState.getCollisionShape(level, pos).isEmpty()) {
/*  88 */       spawnPos = pos;
/*     */     } else {
/*  90 */       spawnPos = pos.relative(clickedFace);
/*     */     } 
/*     */     
/*  93 */     return spawnMob(context.getPlayer(), itemStack, level, spawnPos, true, (!Objects.equals(pos, spawnPos) && clickedFace == Direction.UP));
/*     */   }
/*     */   
/*     */   private InteractionResult spawnMob(LivingEntity user, ItemStack itemStack, Level level, BlockPos spawnPos, boolean tryMoveDown, boolean movedUp) {
/*  97 */     EntityType<?> type = getType(itemStack);
/*  98 */     if (type == null) {
/*  99 */       return InteractionResult.FAIL;
/*     */     }
/* 101 */     if (!type.isAllowedInPeaceful() && level.getDifficulty() == Difficulty.PEACEFUL) {
/* 102 */       return InteractionResult.FAIL;
/*     */     }
/* 104 */     if (type.spawn((ServerLevel)level, itemStack, user, spawnPos, EntitySpawnReason.SPAWN_ITEM_USE, tryMoveDown, movedUp) != null) {
/* 105 */       itemStack.consume(1, user);
/* 106 */       level.gameEvent(user, GameEvent.ENTITY_PLACE, spawnPos);
/*     */     } 
/*     */     
/* 109 */     return InteractionResult.SUCCESS;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public InteractionResult use(Level level, Player player, InteractionHand hand) {
/* 115 */     ItemStack itemStack = player.getItemInHand(hand);
/*     */     
/* 117 */     BlockHitResult hitResult = getPlayerPOVHitResult(level, player, ClipContext.Fluid.SOURCE_ONLY);
/* 118 */     if (hitResult.getType() != HitResult.Type.BLOCK) {
/* 119 */       return InteractionResult.PASS;
/*     */     }
/*     */     
/* 122 */     if (level instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)level; }
/* 123 */     else { return InteractionResult.SUCCESS; }
/*     */ 
/*     */     
/* 126 */     BlockPos pos = hitResult.getBlockPos();
/* 127 */     if (!(level.getBlockState(pos).getBlock() instanceof net.minecraft.world.level.block.LiquidBlock)) {
/* 128 */       return InteractionResult.PASS;
/*     */     }
/* 130 */     if (!level.mayInteract(player, pos) || !player.mayUseItemAt(pos, hitResult.getDirection(), itemStack)) {
/* 131 */       return InteractionResult.FAIL;
/*     */     }
/* 133 */     InteractionResult result = spawnMob(player, itemStack, level, pos, false, false);
/* 134 */     if (result == InteractionResult.SUCCESS) {
/* 135 */       player.awardStat(Stats.ITEM_USED.get(this));
/*     */     }
/* 137 */     return result;
/*     */   }
/*     */ 
/*     */   
/* 141 */   public boolean spawnsEntity(ItemStack itemStack, EntityType<?> type) { return Objects.equals(getType(itemStack), type); }
/*     */ 
/*     */ 
/*     */   
/* 145 */   public static SpawnEggItem byId(EntityType<?> type) { return (SpawnEggItem)BY_ID.get(type); }
/*     */ 
/*     */ 
/*     */   
/* 149 */   public static Iterable<SpawnEggItem> eggs() { return Iterables.unmodifiableIterable(BY_ID.values()); }
/*     */ 
/*     */   
/*     */   public EntityType<?> getType(ItemStack itemStack) {
/* 153 */     TypedEntityData<EntityType<?>> entityData = (TypedEntityData)itemStack.get(DataComponents.ENTITY_DATA);
/* 154 */     if (entityData != null) {
/* 155 */       return (EntityType)entityData.type();
/*     */     }
/* 157 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 162 */   public FeatureFlagSet requiredFeatures() { return (FeatureFlagSet)Optional.ofNullable((TypedEntityData)components().get(DataComponents.ENTITY_DATA)).map(TypedEntityData::type).map(EntityType::requiredFeatures).orElseGet(FeatureFlagSet::of); }
/*     */   
/*     */   public Optional<Mob> spawnOffspringFromSpawnEgg(Player player, Mob parent, EntityType<? extends Mob> type, ServerLevel level, Vec3 pos, ItemStack spawnEggStack) {
/*     */     Mob offspring;
/* 166 */     if (!spawnsEntity(spawnEggStack, type)) {
/* 167 */       return Optional.empty();
/*     */     }
/*     */ 
/*     */     
/* 171 */     if (parent instanceof AgeableMob) {
/* 172 */       offspring = ((AgeableMob)parent).getBreedOffspring(level, (AgeableMob)parent);
/*     */     } else {
/* 174 */       offspring = (Mob)type.create(level, EntitySpawnReason.SPAWN_ITEM_USE);
/*     */     } 
/* 176 */     if (offspring == null) {
/* 177 */       return Optional.empty();
/*     */     }
/*     */     
/* 180 */     offspring.setBaby(true);
/* 181 */     if (!offspring.isBaby()) {
/* 182 */       return Optional.empty();
/*     */     }
/*     */     
/* 185 */     offspring.snapTo(pos.x(), pos.y(), pos.z(), 0.0F, 0.0F);
/* 186 */     offspring.applyComponentsFromItemStack(spawnEggStack);
/*     */     
/* 188 */     level.addFreshEntityWithPassengers(offspring);
/* 189 */     spawnEggStack.consume(1, player);
/* 190 */     return Optional.of(offspring);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean shouldPrintOpWarning(ItemStack stack, Player player) {
/* 195 */     if (player != null && player.permissions().hasPermission(Permissions.COMMANDS_GAMEMASTER)) {
/* 196 */       TypedEntityData<EntityType<?>> entityData = (TypedEntityData)stack.get(DataComponents.ENTITY_DATA);
/* 197 */       if (entityData != null) {
/* 198 */         return ((EntityType)entityData.type()).onlyOpCanSetNbt();
/*     */       }
/*     */     } 
/*     */     
/* 202 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\item\SpawnEggItem.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */