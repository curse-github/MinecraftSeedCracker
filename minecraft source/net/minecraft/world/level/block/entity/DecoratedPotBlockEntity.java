/*     */ package net.minecraft.world.level.block.entity;
/*     */ 
/*     */ import java.util.List;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.HolderLookup;
/*     */ import net.minecraft.core.component.DataComponentGetter;
/*     */ import net.minecraft.core.component.DataComponentMap;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.network.protocol.Packet;
/*     */ import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.world.RandomizableContainer;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.item.component.ItemContainerContents;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ import net.minecraft.world.level.storage.loot.LootTable;
/*     */ import net.minecraft.world.ticks.ContainerSingleItem;
/*     */ 
/*     */ public class DecoratedPotBlockEntity
/*     */   extends BlockEntity
/*     */   implements ContainerSingleItem.BlockContainerSingleItem, RandomizableContainer {
/*     */   public static final String TAG_SHERDS = "sherds";
/*     */   public static final String TAG_ITEM = "item";
/*     */   public static final int EVENT_POT_WOBBLES = 1;
/*     */   public long wobbleStartedAtTick;
/*     */   public WobbleStyle lastWobbleStyle;
/*     */   private PotDecorations decorations;
/*  34 */   private ItemStack item = ItemStack.EMPTY;
/*     */   protected ResourceKey<LootTable> lootTable;
/*     */   protected long lootTableSeed;
/*     */   
/*     */   public DecoratedPotBlockEntity(BlockPos worldPosition, BlockState blockState) {
/*  39 */     super(BlockEntityType.DECORATED_POT, worldPosition, blockState);
/*  40 */     this.decorations = PotDecorations.EMPTY;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void saveAdditional(ValueOutput output) {
/*  45 */     super.saveAdditional(output);
/*  46 */     if (!this.decorations.equals(PotDecorations.EMPTY)) {
/*  47 */       output.store("sherds", PotDecorations.CODEC, this.decorations);
/*     */     }
/*     */     
/*  50 */     if (!trySaveLootTable(output) && !this.item.isEmpty()) {
/*  51 */       output.store("item", ItemStack.CODEC, this.item);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected void loadAdditional(ValueInput input) {
/*  57 */     super.loadAdditional(input);
/*  58 */     this.decorations = (PotDecorations)input.read("sherds", PotDecorations.CODEC).orElse(PotDecorations.EMPTY);
/*     */     
/*  60 */     if (!tryLoadLootTable(input)) {
/*  61 */       this.item = (ItemStack)input.read("item", ItemStack.CODEC).orElse(ItemStack.EMPTY);
/*     */     } else {
/*  63 */       this.item = ItemStack.EMPTY;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  69 */   public ClientboundBlockEntityDataPacket getUpdatePacket() { return ClientboundBlockEntityDataPacket.create(this); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  74 */   public CompoundTag getUpdateTag(HolderLookup.Provider registries) { return saveCustomOnly(registries); }
/*     */ 
/*     */ 
/*     */   
/*  78 */   public Direction getDirection() { return (Direction)getBlockState().getValue(BlockStateProperties.HORIZONTAL_FACING); }
/*     */ 
/*     */ 
/*     */   
/*  82 */   public PotDecorations getDecorations() { return this.decorations; }
/*     */ 
/*     */   
/*     */   public static ItemStack createDecoratedPotItem(PotDecorations decorations) {
/*  86 */     ItemStack potItem = Items.DECORATED_POT.getDefaultInstance();
/*  87 */     potItem.set(DataComponents.POT_DECORATIONS, decorations);
/*  88 */     return potItem;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  93 */   public ResourceKey<LootTable> getLootTable() { return this.lootTable; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  98 */   public void setLootTable(ResourceKey<LootTable> lootTable) { this.lootTable = lootTable; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 103 */   public long getLootTableSeed() { return this.lootTableSeed; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 108 */   public void setLootTableSeed(long lootTableSeed) { this.lootTableSeed = lootTableSeed; }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void collectImplicitComponents(DataComponentMap.Builder components) {
/* 113 */     super.collectImplicitComponents(components);
/* 114 */     components.set(DataComponents.POT_DECORATIONS, this.decorations);
/* 115 */     components.set(DataComponents.CONTAINER, ItemContainerContents.fromItems(List.of(this.item)));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void applyImplicitComponents(DataComponentGetter components) {
/* 120 */     super.applyImplicitComponents(components);
/* 121 */     this.decorations = (PotDecorations)components.getOrDefault(DataComponents.POT_DECORATIONS, PotDecorations.EMPTY);
/* 122 */     this.item = ((ItemContainerContents)components.getOrDefault(DataComponents.CONTAINER, ItemContainerContents.EMPTY)).copyOne();
/*     */   }
/*     */ 
/*     */   
/*     */   public void removeComponentsFromTag(ValueOutput output) {
/* 127 */     super.removeComponentsFromTag(output);
/* 128 */     output.discard("sherds");
/* 129 */     output.discard("item");
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack getTheItem() {
/* 134 */     unpackLootTable(null);
/* 135 */     return this.item;
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack splitTheItem(int count) {
/* 140 */     unpackLootTable(null);
/* 141 */     ItemStack result = this.item.split(count);
/* 142 */     if (this.item.isEmpty()) {
/* 143 */       this.item = ItemStack.EMPTY;
/*     */     }
/* 145 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setTheItem(ItemStack itemStack) {
/* 150 */     unpackLootTable(null);
/* 151 */     this.item = itemStack;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 156 */   public BlockEntity getContainerBlockEntity() { return this; }
/*     */ 
/*     */   
/*     */   public void wobble(WobbleStyle wobbleStyle) {
/* 160 */     if (this.level == null || this.level.isClientSide()) {
/*     */       return;
/*     */     }
/* 163 */     this.level.blockEvent(getBlockPos(), getBlockState().getBlock(), 1, wobbleStyle.ordinal());
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean triggerEvent(int event, int data) {
/* 168 */     if (this.level != null && event == 1 && data >= 0 && data < WobbleStyle.values().length) {
/* 169 */       this.wobbleStartedAtTick = this.level.getGameTime();
/* 170 */       this.lastWobbleStyle = WobbleStyle.values()[data];
/* 171 */       return true;
/*     */     } 
/* 173 */     return super.triggerEvent(event, data);
/*     */   }
/*     */   
/*     */   public enum WobbleStyle {
/* 177 */     POSITIVE(7),
/* 178 */     NEGATIVE(10);
/*     */     
/*     */     public final int duration;
/*     */ 
/*     */     
/* 183 */     WobbleStyle(int duration) { this.duration = duration; }
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\entity\DecoratedPotBlockEntity.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */