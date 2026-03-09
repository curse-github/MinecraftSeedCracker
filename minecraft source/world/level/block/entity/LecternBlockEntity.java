/*     */ package net.minecraft.world.level.block.entity;
/*     */ 
/*     */ import net.minecraft.commands.CommandSource;
/*     */ import net.minecraft.commands.CommandSourceStack;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.server.permissions.LevelBasedPermissionSet;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.Clearable;
/*     */ import net.minecraft.world.Container;
/*     */ import net.minecraft.world.MenuProvider;
/*     */ import net.minecraft.world.entity.item.ItemEntity;
/*     */ import net.minecraft.world.entity.player.Inventory;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.inventory.AbstractContainerMenu;
/*     */ import net.minecraft.world.inventory.ContainerData;
/*     */ import net.minecraft.world.inventory.LecternMenu;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.component.WritableBookContent;
/*     */ import net.minecraft.world.item.component.WrittenBookContent;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.LecternBlock;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ import net.minecraft.world.phys.Vec2;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class LecternBlockEntity
/*     */   extends BlockEntity
/*     */   implements Clearable, MenuProvider {
/*     */   public static final int DATA_PAGE = 0;
/*     */   public static final int NUM_DATA = 1;
/*     */   public static final int SLOT_BOOK = 0;
/*     */   public static final int NUM_SLOTS = 1;
/*     */   
/*  40 */   private final Container bookAccess = new Container()
/*     */     {
/*     */       public int getContainerSize() {
/*  43 */         return 1;
/*     */       }
/*     */ 
/*     */ 
/*     */       
/*  48 */       public boolean isEmpty() { return LecternBlockEntity.this.book.isEmpty(); }
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  53 */       public ItemStack getItem(int slot) { return (slot == 0) ? LecternBlockEntity.this.book : ItemStack.EMPTY; }
/*     */ 
/*     */ 
/*     */       
/*     */       public ItemStack removeItem(int slot, int count) {
/*  58 */         if (slot == 0) {
/*  59 */           ItemStack result = LecternBlockEntity.this.book.split(count);
/*  60 */           if (LecternBlockEntity.this.book.isEmpty()) {
/*  61 */             LecternBlockEntity.this.onBookItemRemove();
/*     */           }
/*  63 */           return result;
/*     */         } 
/*  65 */         return ItemStack.EMPTY;
/*     */       }
/*     */ 
/*     */       
/*     */       public ItemStack removeItemNoUpdate(int slot) {
/*  70 */         if (slot == 0) {
/*  71 */           ItemStack prev = LecternBlockEntity.this.book;
/*  72 */           LecternBlockEntity.this.book = ItemStack.EMPTY;
/*  73 */           LecternBlockEntity.this.onBookItemRemove();
/*  74 */           return prev;
/*     */         } 
/*  76 */         return ItemStack.EMPTY;
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       public void setItem(int slot, ItemStack itemStack) {}
/*     */ 
/*     */ 
/*     */       
/*  86 */       public int getMaxStackSize() { return 1; }
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  91 */       public void setChanged() { LecternBlockEntity.this.setChanged(); }
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  96 */       public boolean stillValid(Player player) { return (stillValidBlockEntity(LecternBlockEntity.this, player) && LecternBlockEntity.this.hasBook()); }
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 101 */       public boolean canPlaceItem(int slot, ItemStack itemStack) { return false; }
/*     */ 
/*     */ 
/*     */       
/*     */       public void clearContent() {}
/*     */     };
/*     */ 
/*     */   
/* 109 */   private final ContainerData dataAccess = new ContainerData()
/*     */     {
/*     */       public int get(int dataId) {
/* 112 */         return (dataId == 0) ? LecternBlockEntity.this.page : 0;
/*     */       }
/*     */ 
/*     */       
/*     */       public void set(int dataId, int value) {
/* 117 */         if (dataId == 0) {
/* 118 */           LecternBlockEntity.this.setPage(value);
/*     */         }
/*     */       }
/*     */ 
/*     */ 
/*     */       
/* 124 */       public int getCount() { return 1; }
/*     */     };
/*     */ 
/*     */   
/* 128 */   private ItemStack book = ItemStack.EMPTY;
/*     */   
/*     */   private int page;
/*     */   private int pageCount;
/*     */   
/* 133 */   public LecternBlockEntity(BlockPos worldPosition, BlockState blockState) { super(BlockEntityType.LECTERN, worldPosition, blockState); }
/*     */ 
/*     */ 
/*     */   
/* 137 */   public ItemStack getBook() { return this.book; }
/*     */ 
/*     */ 
/*     */   
/* 141 */   public boolean hasBook() { return (this.book.has(DataComponents.WRITABLE_BOOK_CONTENT) || this.book.has(DataComponents.WRITTEN_BOOK_CONTENT)); }
/*     */ 
/*     */ 
/*     */   
/* 145 */   public void setBook(ItemStack book) { setBook(book, null); }
/*     */ 
/*     */   
/*     */   private void onBookItemRemove() {
/* 149 */     this.page = 0;
/* 150 */     this.pageCount = 0;
/* 151 */     LecternBlock.resetBookState(null, getLevel(), getBlockPos(), getBlockState(), false);
/*     */   }
/*     */   
/*     */   public void setBook(ItemStack book, Player resolutionContext) {
/* 155 */     this.book = resolveBook(book, resolutionContext);
/* 156 */     this.page = 0;
/* 157 */     this.pageCount = getPageCount(this.book);
/* 158 */     setChanged();
/*     */   }
/*     */   
/*     */   private void setPage(int page) {
/* 162 */     int newPage = Mth.clamp(page, 0, this.pageCount - 1);
/* 163 */     if (newPage != this.page) {
/* 164 */       this.page = newPage;
/* 165 */       setChanged();
/* 166 */       LecternBlock.signalPageChange(getLevel(), getBlockPos(), getBlockState());
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/* 171 */   public int getPage() { return this.page; }
/*     */ 
/*     */   
/*     */   public int getRedstoneSignal() {
/* 175 */     float pageProgress = (this.pageCount > 1) ? (getPage() / (this.pageCount - 1.0F)) : 1.0F;
/* 176 */     return Mth.floor(pageProgress * 14.0F) + (hasBook() ? 1 : 0);
/*     */   }
/*     */   
/*     */   private ItemStack resolveBook(ItemStack book, Player player) {
/* 180 */     Level level = this.level; if (level instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)level;
/* 181 */       WrittenBookContent.resolveForItem(book, createCommandSourceStack(player, serverLevel), player); }
/*     */     
/* 183 */     return book;
/*     */   }
/*     */   
/*     */   private CommandSourceStack createCommandSourceStack(Player player, ServerLevel level) {
/*     */     Component displayName;
/*     */     String textName;
/* 189 */     if (player == null) {
/* 190 */       textName = "Lectern";
/* 191 */       displayName = Component.literal("Lectern");
/*     */     } else {
/* 193 */       textName = player.getPlainTextName();
/* 194 */       displayName = player.getDisplayName();
/*     */     } 
/* 196 */     Vec3 pos = Vec3.atCenterOf(this.worldPosition);
/* 197 */     return new CommandSourceStack(CommandSource.NULL, pos, Vec2.ZERO, level, LevelBasedPermissionSet.GAMEMASTER, textName, displayName, level.getServer(), player);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void loadAdditional(ValueInput input) {
/* 202 */     super.loadAdditional(input);
/*     */     
/* 204 */     this
/*     */       
/* 206 */       .book = (ItemStack)input.read("Book", ItemStack.CODEC).map(book -> resolveBook(book, null)).orElse(ItemStack.EMPTY);
/*     */     
/* 208 */     this.pageCount = getPageCount(this.book);
/* 209 */     this.page = Mth.clamp(input.getIntOr("Page", 0), 0, this.pageCount - 1);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void saveAdditional(ValueOutput output) {
/* 214 */     super.saveAdditional(output);
/*     */     
/* 216 */     if (!getBook().isEmpty()) {
/* 217 */       output.store("Book", ItemStack.CODEC, getBook());
/* 218 */       output.putInt("Page", this.page);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 224 */   public void clearContent() { setBook(ItemStack.EMPTY); }
/*     */ 
/*     */ 
/*     */   
/*     */   public void preRemoveSideEffects(BlockPos pos, BlockState state) {
/* 229 */     if (((Boolean)state.getValue(LecternBlock.HAS_BOOK)).booleanValue() && this.level != null) {
/* 230 */       Direction direction = (Direction)state.getValue(LecternBlock.FACING);
/* 231 */       ItemStack book = getBook().copy();
/* 232 */       float xo = 0.25F * direction.getStepX();
/* 233 */       float zo = 0.25F * direction.getStepZ();
/* 234 */       ItemEntity entity = new ItemEntity(this.level, pos.getX() + 0.5D + xo, (pos.getY() + 1), pos.getZ() + 0.5D + zo, book);
/* 235 */       entity.setDefaultPickUpDelay();
/* 236 */       this.level.addFreshEntity(entity);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 242 */   public AbstractContainerMenu createMenu(int containerId, Inventory inventory, Player player) { return new LecternMenu(containerId, this.bookAccess, this.dataAccess); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 247 */   public Component getDisplayName() { return Component.translatable("container.lectern"); }
/*     */ 
/*     */   
/*     */   private static int getPageCount(ItemStack book) {
/* 251 */     WrittenBookContent writtenContent = (WrittenBookContent)book.get(DataComponents.WRITTEN_BOOK_CONTENT);
/* 252 */     if (writtenContent != null) {
/* 253 */       return writtenContent.pages().size();
/*     */     }
/* 255 */     WritableBookContent writableContent = (WritableBookContent)book.get(DataComponents.WRITABLE_BOOK_CONTENT);
/* 256 */     if (writableContent != null) {
/* 257 */       return writableContent.pages().size();
/*     */     }
/* 259 */     return 0;
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\entity\LecternBlockEntity.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */