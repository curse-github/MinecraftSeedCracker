/*     */ package net.minecraft.world.item;
/*     */ 
/*     */ import java.util.Collection;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.component.DebugStickState;
/*     */ import net.minecraft.world.item.context.UseOnContext;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ 
/*     */ 
/*     */ public class DebugStickItem
/*     */   extends Item
/*     */ {
/*  26 */   public DebugStickItem(Item.Properties properties) { super(properties); }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean canDestroyBlock(ItemStack itemStack, BlockState state, Level level, BlockPos pos, LivingEntity user) {
/*  31 */     if (!level.isClientSide() && user instanceof Player) { Player player = (Player)user;
/*  32 */       handleInteraction(player, state, level, pos, false, itemStack); }
/*     */ 
/*     */     
/*  35 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public InteractionResult useOn(UseOnContext context) {
/*  40 */     Player player = context.getPlayer();
/*  41 */     Level level = context.getLevel();
/*     */     
/*  43 */     if (!level.isClientSide() && player != null) {
/*  44 */       BlockPos pos = context.getClickedPos();
/*  45 */       if (!handleInteraction(player, level.getBlockState(pos), level, pos, true, context.getItemInHand())) {
/*  46 */         return InteractionResult.FAIL;
/*     */       }
/*     */     } 
/*     */     
/*  50 */     return InteractionResult.SUCCESS;
/*     */   }
/*     */   
/*     */   private boolean handleInteraction(Player player, BlockState state, LevelAccessor level, BlockPos pos, boolean cycle, ItemStack itemStackInHand) {
/*  54 */     if (!player.canUseGameMasterBlocks()) {
/*  55 */       return false;
/*     */     }
/*     */     
/*  58 */     Holder<Block> block = state.getBlockHolder();
/*  59 */     StateDefinition<Block, BlockState> definition = ((Block)block.value()).getStateDefinition();
/*  60 */     Collection<Property<?>> properties = definition.getProperties();
/*     */     
/*  62 */     if (properties.isEmpty()) {
/*  63 */       message(player, Component.translatable(this.descriptionId + ".empty", new Object[] { block.getRegisteredName() }));
/*  64 */       return false;
/*     */     } 
/*     */     
/*  67 */     DebugStickState debugStickState = (DebugStickState)itemStackInHand.get(DataComponents.DEBUG_STICK_STATE);
/*  68 */     if (debugStickState == null) {
/*  69 */       return false;
/*     */     }
/*     */     
/*  72 */     Property<?> property = (Property)debugStickState.properties().get(block);
/*  73 */     if (cycle) {
/*  74 */       if (property == null) {
/*  75 */         property = (Property)properties.iterator().next();
/*     */       }
/*     */       
/*  78 */       BlockState newState = cycleState(state, property, player.isSecondaryUseActive());
/*  79 */       level.setBlock(pos, newState, 18);
/*  80 */       message(player, Component.translatable(this.descriptionId + ".update", new Object[] { property.getName(), getNameHelper(newState, property) }));
/*     */     } else {
/*  82 */       property = (Property)getRelative(properties, property, player.isSecondaryUseActive());
/*  83 */       itemStackInHand.set(DataComponents.DEBUG_STICK_STATE, debugStickState.withProperty(block, property));
/*  84 */       message(player, Component.translatable(this.descriptionId + ".select", new Object[] { property.getName(), getNameHelper(state, property) }));
/*     */     } 
/*  86 */     return true;
/*     */   }
/*     */ 
/*     */   
/*  90 */   private static <T extends Comparable<T>> BlockState cycleState(BlockState state, Property<T> property, boolean backward) { return (BlockState)state.setValue(property, (Comparable)getRelative(property.getPossibleValues(), state.getValue(property), backward)); }
/*     */ 
/*     */ 
/*     */   
/*  94 */   private static <T> T getRelative(Iterable<T> collection, T current, boolean backward) { return (T)(backward ? Util.findPreviousInIterable(collection, current) : Util.findNextInIterable(collection, current)); }
/*     */ 
/*     */ 
/*     */   
/*  98 */   private static void message(Player player, Component message) { ((ServerPlayer)player).sendSystemMessage(message, true); }
/*     */ 
/*     */ 
/*     */   
/* 102 */   private static <T extends Comparable<T>> String getNameHelper(BlockState state, Property<T> property) { return property.getName(state.getValue(property)); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\item\DebugStickItem.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */