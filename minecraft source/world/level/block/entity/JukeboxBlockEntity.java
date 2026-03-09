/*     */ package net.minecraft.world.level.block.entity;
/*     */ 
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.world.Container;
/*     */ import net.minecraft.world.entity.item.ItemEntity;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.JukeboxSong;
/*     */ import net.minecraft.world.item.JukeboxSongPlayer;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.JukeboxBlock;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.gameevent.GameEvent;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import net.minecraft.world.ticks.ContainerSingleItem;
/*     */ 
/*     */ 
/*     */ public class JukeboxBlockEntity
/*     */   extends BlockEntity
/*     */   implements ContainerSingleItem.BlockContainerSingleItem
/*     */ {
/*     */   public static final String SONG_ITEM_TAG_ID = "RecordItem";
/*     */   public static final String TICKS_SINCE_SONG_STARTED_TAG_ID = "ticks_since_song_started";
/*  29 */   private ItemStack item = ItemStack.EMPTY;
/*  30 */   private final JukeboxSongPlayer jukeboxSongPlayer = new JukeboxSongPlayer(this::onSongChanged, getBlockPos());
/*     */ 
/*     */   
/*  33 */   public JukeboxBlockEntity(BlockPos worldPosition, BlockState blockState) { super(BlockEntityType.JUKEBOX, worldPosition, blockState); }
/*     */ 
/*     */ 
/*     */   
/*  37 */   public JukeboxSongPlayer getSongPlayer() { return this.jukeboxSongPlayer; }
/*     */ 
/*     */   
/*     */   public void onSongChanged() {
/*  41 */     this.level.updateNeighborsAt(getBlockPos(), getBlockState().getBlock());
/*  42 */     setChanged();
/*     */   }
/*     */   
/*     */   private void notifyItemChangedInJukebox(boolean wasInserted) {
/*  46 */     if (this.level == null || this.level.getBlockState(getBlockPos()) != getBlockState()) {
/*     */       return;
/*     */     }
/*     */     
/*  50 */     this.level.setBlock(getBlockPos(), (BlockState)getBlockState().setValue(JukeboxBlock.HAS_RECORD, Boolean.valueOf(wasInserted)), 2);
/*  51 */     this.level.gameEvent(GameEvent.BLOCK_CHANGE, getBlockPos(), GameEvent.Context.of(getBlockState()));
/*     */   }
/*     */   
/*     */   public void popOutTheItem() {
/*  55 */     if (this.level == null || this.level.isClientSide()) {
/*     */       return;
/*     */     }
/*     */     
/*  59 */     BlockPos pos = getBlockPos();
/*  60 */     ItemStack itemBeforePoppingOut = getTheItem();
/*  61 */     if (itemBeforePoppingOut.isEmpty()) {
/*     */       return;
/*     */     }
/*     */     
/*  65 */     removeTheItem();
/*     */     
/*  67 */     Vec3 itemPos = Vec3.atLowerCornerWithOffset(pos, 0.5D, 1.01D, 0.5D).offsetRandomXZ(this.level.random, 0.7F);
/*  68 */     ItemStack itemStack = itemBeforePoppingOut.copy();
/*     */     
/*  70 */     ItemEntity entity = new ItemEntity(this.level, itemPos.x(), itemPos.y(), itemPos.z(), itemStack);
/*  71 */     entity.setDefaultPickUpDelay();
/*  72 */     this.level.addFreshEntity(entity);
/*  73 */     onSongChanged();
/*     */   }
/*     */ 
/*     */   
/*  77 */   public static void tick(Level level, BlockPos blockPos, BlockState blockState, JukeboxBlockEntity jukebox) { jukebox.jukeboxSongPlayer.tick(level, blockState); }
/*     */ 
/*     */ 
/*     */   
/*  81 */   public int getComparatorOutput() { return ((Integer)JukeboxSong.fromStack(this.level.registryAccess(), this.item).map(Holder::value).map(JukeboxSong::comparatorOutput).orElse(Integer.valueOf(0))).intValue(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  86 */   protected void loadAdditional(ValueInput input) { super.loadAdditional(input);
/*     */     
/*  88 */     ItemStack newItem = (ItemStack)input.read("RecordItem", ItemStack.CODEC).orElse(ItemStack.EMPTY);
/*  89 */     if (!this.item.isEmpty() && !ItemStack.isSameItemSameComponents(newItem, this.item)) {
/*  90 */       this.jukeboxSongPlayer.stop(this.level, getBlockState());
/*     */     }
/*  92 */     this.item = newItem;
/*     */     
/*  94 */     input.getLong("ticks_since_song_started").ifPresent(ticksSinceSongStarted -> 
/*  95 */         JukeboxSong.fromStack(input.lookup(), this.item).ifPresent(())); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void saveAdditional(ValueOutput output) {
/* 101 */     super.saveAdditional(output);
/*     */     
/* 103 */     if (!getTheItem().isEmpty()) {
/* 104 */       output.store("RecordItem", ItemStack.CODEC, getTheItem());
/*     */     }
/*     */     
/* 107 */     if (this.jukeboxSongPlayer.getSong() != null) {
/* 108 */       output.putLong("ticks_since_song_started", this.jukeboxSongPlayer.getTicksSinceSongStarted());
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 114 */   public ItemStack getTheItem() { return this.item; }
/*     */ 
/*     */ 
/*     */   
/*     */   public ItemStack splitTheItem(int count) {
/* 119 */     ItemStack retrievedItem = this.item;
/* 120 */     setTheItem(ItemStack.EMPTY);
/* 121 */     return retrievedItem;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setTheItem(ItemStack itemStack) {
/* 126 */     this.item = itemStack;
/*     */     
/* 128 */     boolean itemWasInserted = !this.item.isEmpty();
/* 129 */     Optional<Holder<JukeboxSong>> maybeSong = JukeboxSong.fromStack(this.level.registryAccess(), this.item);
/*     */     
/* 131 */     notifyItemChangedInJukebox(itemWasInserted);
/* 132 */     if (itemWasInserted && maybeSong.isPresent()) {
/* 133 */       this.jukeboxSongPlayer.play(this.level, (Holder)maybeSong.get());
/*     */     } else {
/* 135 */       this.jukeboxSongPlayer.stop(this.level, getBlockState());
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void setRemoved() {
/* 141 */     super.setRemoved();
/* 142 */     this.level.gameEvent(GameEvent.JUKEBOX_STOP_PLAY, getBlockPos(), GameEvent.Context.of(getBlockState()));
/* 143 */     this.level.levelEvent(1011, getBlockPos(), 0);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 148 */   public int getMaxStackSize() { return 1; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 153 */   public BlockEntity getContainerBlockEntity() { return this; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 158 */   public boolean canPlaceItem(int slot, ItemStack itemStack) { return (itemStack.has(DataComponents.JUKEBOX_PLAYABLE) && getItem(slot).isEmpty()); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 163 */   public boolean canTakeItem(Container into, int slot, ItemStack itemStack) { return into.hasAnyMatching(ItemStack::isEmpty); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 168 */   public void preRemoveSideEffects(BlockPos pos, BlockState state) { popOutTheItem(); }
/*     */ 
/*     */   
/*     */   @VisibleForTesting
/*     */   public void setSongItemWithoutPlaying(ItemStack itemStack) {
/* 173 */     this.item = itemStack;
/* 174 */     JukeboxSong.fromStack(this.level.registryAccess(), itemStack).ifPresent(song -> this.jukeboxSongPlayer.setSongWithoutPlaying(song, 0L));
/* 175 */     this.level.updateNeighborsAt(getBlockPos(), getBlockState().getBlock());
/* 176 */     setChanged();
/*     */   }
/*     */ 
/*     */   
/*     */   @VisibleForTesting
/* 181 */   public void tryForcePlaySong() { JukeboxSong.fromStack(this.level.registryAccess(), getTheItem()).ifPresent(song -> this.jukeboxSongPlayer.play(this.level, song)); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\entity\JukeboxBlockEntity.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */