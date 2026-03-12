/*     */ package net.minecraft.world.level.block.entity;
/*     */ 
/*     */ import com.mojang.logging.LogUtils;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectArrayList;
/*     */ import java.util.Objects;
/*     */ import net.minecraft.advancements.CriteriaTriggers;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.HolderLookup;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.nbt.NbtOps;
/*     */ import net.minecraft.nbt.Tag;
/*     */ import net.minecraft.network.protocol.Packet;
/*     */ import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
/*     */ import net.minecraft.resources.RegistryOps;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.item.ItemEntity;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.BrushableBlock;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ import net.minecraft.world.level.storage.loot.LootParams;
/*     */ import net.minecraft.world.level.storage.loot.LootTable;
/*     */ import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
/*     */ import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class BrushableBlockEntity
/*     */   extends BlockEntity
/*     */ {
/*  42 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*     */   private static final String LOOT_TABLE_TAG = "LootTable";
/*     */   private static final String LOOT_TABLE_SEED_TAG = "LootTableSeed";
/*     */   private static final String HIT_DIRECTION_TAG = "hit_direction";
/*     */   private static final String ITEM_TAG = "item";
/*     */   private static final int BRUSH_COOLDOWN_TICKS = 10;
/*     */   private static final int BRUSH_RESET_TICKS = 40;
/*     */   private static final int REQUIRED_BRUSHES_TO_BREAK = 10;
/*     */   private int brushCount;
/*     */   private long brushCountResetsAtTick;
/*     */   private long coolDownEndsAtTick;
/*  54 */   private ItemStack item = ItemStack.EMPTY;
/*     */   
/*     */   private Direction hitDirection;
/*     */   private ResourceKey<LootTable> lootTable;
/*     */   private long lootTableSeed;
/*     */   
/*  60 */   public BrushableBlockEntity(BlockPos worldPosition, BlockState blockState) { super(BlockEntityType.BRUSHABLE_BLOCK, worldPosition, blockState); }
/*     */ 
/*     */   
/*     */   public boolean brush(long gameTime, ServerLevel level, LivingEntity user, Direction direction, ItemStack brush) {
/*  64 */     if (this.hitDirection == null) {
/*  65 */       this.hitDirection = direction;
/*     */     }
/*  67 */     this.brushCountResetsAtTick = gameTime + 40L;
/*     */     
/*  69 */     if (gameTime < this.coolDownEndsAtTick) {
/*  70 */       return false;
/*     */     }
/*  72 */     this.coolDownEndsAtTick = gameTime + 10L;
/*     */     
/*  74 */     unpackLootTable(level, user, brush);
/*     */     
/*  76 */     int previousCompletionState = getCompletionState();
/*     */     
/*  78 */     if (++this.brushCount >= 10) {
/*  79 */       brushingCompleted(level, user, brush);
/*  80 */       return true;
/*     */     } 
/*     */     
/*  83 */     level.scheduleTick(getBlockPos(), getBlockState().getBlock(), 2);
/*     */     
/*  85 */     int completionState = getCompletionState();
/*  86 */     if (previousCompletionState != completionState) {
/*  87 */       BlockState previousState = getBlockState();
/*  88 */       BlockState state = (BlockState)previousState.setValue(BlockStateProperties.DUSTED, Integer.valueOf(completionState));
/*  89 */       level.setBlock(getBlockPos(), state, 3);
/*     */     } 
/*     */     
/*  92 */     return false;
/*     */   }
/*     */   
/*     */   private void unpackLootTable(ServerLevel level, LivingEntity user, ItemStack brush) {
/*  96 */     if (this.lootTable == null) {
/*     */       return;
/*     */     }
/*     */     
/* 100 */     LootTable lootTable = level.getServer().reloadableRegistries().getLootTable(this.lootTable);
/* 101 */     if (user instanceof ServerPlayer) { ServerPlayer serverPlayer = (ServerPlayer)user;
/* 102 */       CriteriaTriggers.GENERATE_LOOT.trigger(serverPlayer, this.lootTable); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 110 */     LootParams params = (new LootParams.Builder(level)).withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(this.worldPosition)).withLuck(user.getLuck()).withParameter(LootContextParams.THIS_ENTITY, user).withParameter(LootContextParams.TOOL, brush).create(LootContextParamSets.ARCHAEOLOGY);
/*     */     
/* 112 */     ObjectArrayList<ItemStack> loot = lootTable.getRandomItems(params, this.lootTableSeed);
/*     */     
/* 114 */     switch (loot.size()) { case 0: 
/*     */       case 1:
/*     */       
/*     */       default:
/* 118 */         LOGGER.warn("Expected max 1 loot from loot table {}, but got {}", this.lootTable.identifier(), Integer.valueOf(loot.size())); break; }
/* 119 */      this.item = (ItemStack)loot.getFirst();
/*     */ 
/*     */ 
/*     */     
/* 123 */     this.lootTable = null;
/* 124 */     setChanged();
/*     */   }
/*     */   private void brushingCompleted(ServerLevel level, LivingEntity user, ItemStack brush) {
/*     */     Block turnsInto;
/* 128 */     dropContent(level, user, brush);
/* 129 */     BlockState blockState = getBlockState();
/* 130 */     level.levelEvent(3008, getBlockPos(), Block.getId(blockState));
/* 131 */     Block block = getBlockState().getBlock();
/*     */ 
/*     */     
/* 134 */     if (block instanceof BrushableBlock) { BrushableBlock brushableBlock = (BrushableBlock)block;
/* 135 */       turnsInto = brushableBlock.getTurnsInto(); }
/*     */     else
/* 137 */     { turnsInto = Blocks.AIR; }
/*     */ 
/*     */     
/* 140 */     level.setBlock(this.worldPosition, turnsInto.defaultBlockState(), 3);
/*     */   }
/*     */   
/*     */   private void dropContent(ServerLevel level, LivingEntity user, ItemStack brush) {
/* 144 */     unpackLootTable(level, user, brush);
/*     */     
/* 146 */     if (!this.item.isEmpty()) {
/* 147 */       double size = EntityType.ITEM.getWidth();
/* 148 */       double centerRange = 1.0D - size;
/* 149 */       double halfSize = size / 2.0D;
/*     */       
/* 151 */       Direction dropDirection = (Direction)Objects.requireNonNullElse(this.hitDirection, Direction.UP);
/* 152 */       BlockPos dropPos = this.worldPosition.relative(dropDirection, 1);
/*     */       
/* 154 */       double xo = dropPos.getX() + 0.5D * centerRange + halfSize;
/* 155 */       double yo = dropPos.getY() + 0.5D + (EntityType.ITEM.getHeight() / 2.0F);
/* 156 */       double zo = dropPos.getZ() + 0.5D * centerRange + halfSize;
/*     */       
/* 158 */       ItemEntity entity = new ItemEntity(level, xo, yo, zo, this.item.split(level.random.nextInt(21) + 10));
/* 159 */       entity.setDeltaMovement(Vec3.ZERO);
/* 160 */       level.addFreshEntity(entity);
/*     */       
/* 162 */       this.item = ItemStack.EMPTY;
/*     */     } 
/*     */   }
/*     */   
/*     */   public void checkReset(ServerLevel level) {
/* 167 */     if (this.brushCount != 0 && level.getGameTime() >= this.brushCountResetsAtTick) {
/* 168 */       int previousCompletionState = getCompletionState();
/* 169 */       this.brushCount = Math.max(0, this.brushCount - 2);
/* 170 */       int completionState = getCompletionState();
/*     */       
/* 172 */       if (previousCompletionState != completionState) {
/* 173 */         level.setBlock(getBlockPos(), (BlockState)getBlockState().setValue(BlockStateProperties.DUSTED, Integer.valueOf(completionState)), 3);
/*     */       }
/* 175 */       int retractionSpeed = 4;
/* 176 */       this.brushCountResetsAtTick = level.getGameTime() + 4L;
/*     */     } 
/*     */     
/* 179 */     if (this.brushCount == 0) {
/* 180 */       this.hitDirection = null;
/* 181 */       this.brushCountResetsAtTick = 0L;
/* 182 */       this.coolDownEndsAtTick = 0L;
/*     */     } else {
/* 184 */       level.scheduleTick(getBlockPos(), getBlockState().getBlock(), 2);
/*     */     } 
/*     */   }
/*     */   
/*     */   private boolean tryLoadLootTable(ValueInput input) {
/* 189 */     this.lootTable = (ResourceKey)input.read("LootTable", LootTable.KEY_CODEC).orElse(null);
/* 190 */     this.lootTableSeed = input.getLongOr("LootTableSeed", 0L);
/* 191 */     return (this.lootTable != null);
/*     */   }
/*     */   
/*     */   private boolean trySaveLootTable(ValueOutput base) {
/* 195 */     if (this.lootTable == null) {
/* 196 */       return false;
/*     */     }
/*     */     
/* 199 */     base.store("LootTable", LootTable.KEY_CODEC, this.lootTable);
/* 200 */     if (this.lootTableSeed != 0L) {
/* 201 */       base.putLong("LootTableSeed", this.lootTableSeed);
/*     */     }
/* 203 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
/* 208 */     CompoundTag tag = super.getUpdateTag(registries);
/* 209 */     tag.storeNullable("hit_direction", Direction.LEGACY_ID_CODEC, this.hitDirection);
/* 210 */     if (!this.item.isEmpty()) {
/* 211 */       RegistryOps<Tag> ops = registries.createSerializationContext(NbtOps.INSTANCE);
/* 212 */       tag.store("item", ItemStack.CODEC, ops, this.item);
/*     */     } 
/* 214 */     return tag;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 219 */   public ClientboundBlockEntityDataPacket getUpdatePacket() { return ClientboundBlockEntityDataPacket.create(this); }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void loadAdditional(ValueInput input) {
/* 224 */     super.loadAdditional(input);
/*     */     
/* 226 */     if (!tryLoadLootTable(input)) {
/* 227 */       this.item = (ItemStack)input.read("item", ItemStack.CODEC).orElse(ItemStack.EMPTY);
/*     */     } else {
/* 229 */       this.item = ItemStack.EMPTY;
/*     */     } 
/*     */     
/* 232 */     this.hitDirection = (Direction)input.read("hit_direction", Direction.LEGACY_ID_CODEC).orElse(null);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void saveAdditional(ValueOutput output) {
/* 237 */     super.saveAdditional(output);
/*     */     
/* 239 */     if (!trySaveLootTable(output) && !this.item.isEmpty()) {
/* 240 */       output.store("item", ItemStack.CODEC, this.item);
/*     */     }
/*     */   }
/*     */   
/*     */   public void setLootTable(ResourceKey<LootTable> lootTable, long seed) {
/* 245 */     this.lootTable = lootTable;
/* 246 */     this.lootTableSeed = seed;
/*     */   }
/*     */   
/*     */   private int getCompletionState() {
/* 250 */     if (this.brushCount == 0) {
/* 251 */       return 0;
/*     */     }
/* 253 */     if (this.brushCount < 3) {
/* 254 */       return 1;
/*     */     }
/* 256 */     if (this.brushCount < 6) {
/* 257 */       return 2;
/*     */     }
/* 259 */     return 3;
/*     */   }
/*     */ 
/*     */   
/* 263 */   public Direction getHitDirection() { return this.hitDirection; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 272 */   public ItemStack getItem() { return this.item; }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\entity\BrushableBlockEntity.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */