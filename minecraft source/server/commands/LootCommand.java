/*     */ package net.minecraft.server.commands;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.mojang.brigadier.CommandDispatcher;
/*     */ import com.mojang.brigadier.Message;
/*     */ import com.mojang.brigadier.arguments.IntegerArgumentType;
/*     */ import com.mojang.brigadier.builder.ArgumentBuilder;
/*     */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*     */ import com.mojang.brigadier.builder.RequiredArgumentBuilder;
/*     */ import com.mojang.brigadier.context.CommandContext;
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.commands.CommandBuildContext;
/*     */ import net.minecraft.commands.CommandSourceStack;
/*     */ import net.minecraft.commands.Commands;
/*     */ import net.minecraft.commands.arguments.EntityArgument;
/*     */ import net.minecraft.commands.arguments.ResourceOrIdArgument;
/*     */ import net.minecraft.commands.arguments.SlotArgument;
/*     */ import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
/*     */ import net.minecraft.commands.arguments.coordinates.Vec3Argument;
/*     */ import net.minecraft.commands.arguments.item.ItemArgument;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.world.Container;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EquipmentSlot;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.SlotAccess;
/*     */ import net.minecraft.world.entity.item.ItemEntity;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.storage.loot.LootParams;
/*     */ import net.minecraft.world.level.storage.loot.LootTable;
/*     */ import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
/*     */ import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class LootCommand
/*     */ {
/*  60 */   private static final DynamicCommandExceptionType ERROR_NO_HELD_ITEMS = new DynamicCommandExceptionType(entity -> Component.translatableEscape("commands.drop.no_held_items", new Object[] { entity }));
/*  61 */   private static final DynamicCommandExceptionType ERROR_NO_ENTITY_LOOT_TABLE = new DynamicCommandExceptionType(entity -> Component.translatableEscape("commands.drop.no_loot_table.entity", new Object[] { entity }));
/*  62 */   private static final DynamicCommandExceptionType ERROR_NO_BLOCK_LOOT_TABLE = new DynamicCommandExceptionType(block -> Component.translatableEscape("commands.drop.no_loot_table.block", new Object[] { block }));
/*     */   
/*     */   public static void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext context) {
/*  65 */     dispatcher.register(
/*  66 */         (LiteralArgumentBuilder)addTargets(
/*  67 */           (LiteralArgumentBuilder)Commands.literal("loot")
/*  68 */           .requires(Commands.hasPermission(Commands.LEVEL_GAMEMASTERS)), (target, output) -> 
/*     */           
/*  70 */           target
/*  71 */           .then(
/*  72 */             Commands.literal("fish")
/*  73 */             .then(
/*  74 */               Commands.argument("loot_table", ResourceOrIdArgument.lootTable(context))
/*  75 */               .then((
/*  76 */                 (RequiredArgumentBuilder)((RequiredArgumentBuilder)((RequiredArgumentBuilder)Commands.argument("pos", BlockPosArgument.blockPos())
/*  77 */                 .executes(()))
/*  78 */                 .then(
/*  79 */                   Commands.argument("tool", ItemArgument.item(context))
/*  80 */                   .executes(())))
/*     */                 
/*  82 */                 .then(
/*  83 */                   Commands.literal("mainhand")
/*  84 */                   .executes(())))
/*     */                 
/*  86 */                 .then(
/*  87 */                   Commands.literal("offhand")
/*  88 */                   .executes(())))))
/*     */ 
/*     */ 
/*     */ 
/*     */           
/*  93 */           .then(
/*  94 */             Commands.literal("loot")
/*  95 */             .then(
/*  96 */               Commands.argument("loot_table", ResourceOrIdArgument.lootTable(context))
/*  97 */               .executes(())))
/*     */ 
/*     */           
/* 100 */           .then(
/* 101 */             Commands.literal("kill")
/* 102 */             .then(
/* 103 */               Commands.argument("target", EntityArgument.entity())
/* 104 */               .executes(())))
/*     */ 
/*     */           
/* 107 */           .then(
/* 108 */             Commands.literal("mine")
/* 109 */             .then((
/* 110 */               (RequiredArgumentBuilder)((RequiredArgumentBuilder)((RequiredArgumentBuilder)Commands.argument("pos", BlockPosArgument.blockPos())
/* 111 */               .executes(()))
/* 112 */               .then(
/* 113 */                 Commands.argument("tool", ItemArgument.item(context))
/* 114 */                 .executes(())))
/*     */               
/* 116 */               .then(
/* 117 */                 Commands.literal("mainhand")
/* 118 */                 .executes(())))
/*     */               
/* 120 */               .then(
/* 121 */                 Commands.literal("offhand")
/* 122 */                 .executes(()))))));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 146 */   private static <T extends ArgumentBuilder<CommandSourceStack, T>> T addTargets(T root, TailProvider tail) { return (T)root
/* 147 */       .then((
/* 148 */         (LiteralArgumentBuilder)Commands.literal("replace")
/* 149 */         .then(Commands.literal("entity")
/* 150 */           .then(
/* 151 */             Commands.argument("entities", EntityArgument.entities())
/* 152 */             .then(tail
/* 153 */               .construct(Commands.argument("slot", SlotArgument.slot()), (c, drops, callback) -> 
/* 154 */                 entityReplace(EntityArgument.getEntities(c, "entities"), SlotArgument.getSlot(c, "slot"), drops.size(), drops, callback))
/*     */               
/* 156 */               .then(tail
/* 157 */                 .construct(Commands.argument("count", IntegerArgumentType.integer(0)), (c, drops, callback) -> 
/* 158 */                   entityReplace(EntityArgument.getEntities(c, "entities"), SlotArgument.getSlot(c, "slot"), IntegerArgumentType.getInteger(c, "count"), drops, callback)))))))
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 164 */         .then(Commands.literal("block")
/* 165 */           .then(
/* 166 */             Commands.argument("targetPos", BlockPosArgument.blockPos())
/* 167 */             .then(tail
/* 168 */               .construct(Commands.argument("slot", SlotArgument.slot()), (c, drops, callback) -> 
/* 169 */                 blockReplace((CommandSourceStack)c.getSource(), BlockPosArgument.getLoadedBlockPos(c, "targetPos"), SlotArgument.getSlot(c, "slot"), drops.size(), drops, callback))
/*     */               
/* 171 */               .then(tail
/* 172 */                 .construct(Commands.argument("count", IntegerArgumentType.integer(0)), (c, drops, callback) -> 
/* 173 */                   blockReplace((CommandSourceStack)c.getSource(), BlockPosArgument.getLoadedBlockPos(c, "targetPos"), IntegerArgumentType.getInteger(c, "slot"), IntegerArgumentType.getInteger(c, "count"), drops, callback)))))))
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 180 */       .then(
/* 181 */         Commands.literal("insert")
/* 182 */         .then(tail
/* 183 */           .construct(Commands.argument("targetPos", BlockPosArgument.blockPos()), (c, drops, callback) -> 
/* 184 */             blockDistribute((CommandSourceStack)c.getSource(), BlockPosArgument.getLoadedBlockPos(c, "targetPos"), drops, callback))))
/*     */ 
/*     */ 
/*     */       
/* 188 */       .then(
/* 189 */         Commands.literal("give")
/* 190 */         .then(tail
/* 191 */           .construct(Commands.argument("players", EntityArgument.players()), (c, drops, callback) -> 
/* 192 */             playerGive(EntityArgument.getPlayers(c, "players"), drops, callback))))
/*     */ 
/*     */ 
/*     */       
/* 196 */       .then(
/* 197 */         Commands.literal("spawn")
/* 198 */         .then(tail
/* 199 */           .construct(Commands.argument("targetPos", Vec3Argument.vec3()), (c, drops, callback) -> 
/* 200 */             dropInWorld((CommandSourceStack)c.getSource(), Vec3Argument.getVec3(c, "targetPos"), drops, callback)))); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static Container getContainer(CommandSourceStack source, BlockPos pos) throws CommandSyntaxException {
/* 207 */     BlockEntity blockEntity = source.getLevel().getBlockEntity(pos);
/* 208 */     if (!(blockEntity instanceof Container)) {
/* 209 */       throw ItemCommands.ERROR_TARGET_NOT_A_CONTAINER.create(Integer.valueOf(pos.getX()), Integer.valueOf(pos.getY()), Integer.valueOf(pos.getZ()));
/*     */     }
/*     */     
/* 212 */     return (Container)blockEntity;
/*     */   }
/*     */   
/*     */   private static int blockDistribute(CommandSourceStack source, BlockPos pos, List<ItemStack> drops, Callback callback) throws CommandSyntaxException {
/* 216 */     Container container = getContainer(source, pos);
/*     */     
/* 218 */     List<ItemStack> usedItems = Lists.newArrayListWithCapacity(drops.size());
/* 219 */     for (ItemStack drop : drops) {
/* 220 */       if (distributeToContainer(container, drop.copy())) {
/* 221 */         container.setChanged();
/* 222 */         usedItems.add(drop);
/*     */       } 
/*     */     } 
/*     */     
/* 226 */     callback.accept(usedItems);
/* 227 */     return usedItems.size();
/*     */   }
/*     */   
/*     */   private static boolean distributeToContainer(Container container, ItemStack itemStack) {
/* 231 */     boolean changed = false;
/*     */     
/* 233 */     for (int slot = 0; slot < container.getContainerSize() && !itemStack.isEmpty(); slot++) {
/* 234 */       ItemStack current = container.getItem(slot);
/*     */       
/* 236 */       if (container.canPlaceItem(slot, itemStack)) {
/* 237 */         if (current.isEmpty()) {
/* 238 */           container.setItem(slot, itemStack);
/* 239 */           changed = true; break;
/*     */         } 
/* 241 */         if (canMergeItems(current, itemStack)) {
/* 242 */           int space = itemStack.getMaxStackSize() - current.getCount();
/* 243 */           int count = Math.min(itemStack.getCount(), space);
/*     */           
/* 245 */           itemStack.shrink(count);
/* 246 */           current.grow(count);
/* 247 */           changed = true;
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 252 */     return changed;
/*     */   }
/*     */   
/*     */   private static int blockReplace(CommandSourceStack source, BlockPos pos, int startSlot, int slotCount, List<ItemStack> drops, Callback callback) throws CommandSyntaxException {
/* 256 */     Container container = getContainer(source, pos);
/*     */     
/* 258 */     int maxSlot = container.getContainerSize();
/* 259 */     if (startSlot < 0 || startSlot >= maxSlot) {
/* 260 */       throw ItemCommands.ERROR_TARGET_INAPPLICABLE_SLOT.create(Integer.valueOf(startSlot));
/*     */     }
/*     */     
/* 263 */     List<ItemStack> usedItems = Lists.newArrayListWithCapacity(drops.size());
/*     */     
/* 265 */     for (int i = 0; i < slotCount; i++) {
/* 266 */       int slot = startSlot + i;
/* 267 */       ItemStack toAdd = (i < drops.size()) ? (ItemStack)drops.get(i) : ItemStack.EMPTY;
/*     */       
/* 269 */       if (container.canPlaceItem(slot, toAdd)) {
/* 270 */         container.setItem(slot, toAdd);
/* 271 */         usedItems.add(toAdd);
/*     */       } 
/*     */     } 
/*     */     
/* 275 */     callback.accept(usedItems);
/* 276 */     return usedItems.size();
/*     */   }
/*     */ 
/*     */   
/* 280 */   private static boolean canMergeItems(ItemStack a, ItemStack b) { return (a.getCount() <= a.getMaxStackSize() && ItemStack.isSameItemSameComponents(a, b)); }
/*     */ 
/*     */   
/*     */   private static int playerGive(Collection<ServerPlayer> players, List<ItemStack> drops, Callback callback) throws CommandSyntaxException {
/* 284 */     List<ItemStack> usedItems = Lists.newArrayListWithCapacity(drops.size());
/* 285 */     for (ItemStack drop : drops) {
/* 286 */       for (ServerPlayer player : players) {
/* 287 */         if (player.getInventory().add(drop.copy())) {
/* 288 */           usedItems.add(drop);
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 293 */     callback.accept(usedItems);
/* 294 */     return usedItems.size();
/*     */   }
/*     */   
/*     */   private static void setSlots(Entity entity, List<ItemStack> itemsToSet, int startSlot, int count, List<ItemStack> usedItems) {
/* 298 */     for (int i = 0; i < count; i++) {
/* 299 */       ItemStack item = (i < itemsToSet.size()) ? (ItemStack)itemsToSet.get(i) : ItemStack.EMPTY;
/* 300 */       SlotAccess slotAccess = entity.getSlot(startSlot + i);
/* 301 */       if (slotAccess != null && slotAccess.set(item.copy())) {
/* 302 */         usedItems.add(item);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   private static int entityReplace(Collection<? extends Entity> entities, int startSlot, int count, List<ItemStack> drops, Callback callback) throws CommandSyntaxException {
/* 308 */     List<ItemStack> usedItems = Lists.newArrayListWithCapacity(drops.size());
/*     */     
/* 310 */     for (Entity entity : entities) {
/* 311 */       if (entity instanceof ServerPlayer) { ServerPlayer player = (ServerPlayer)entity;
/* 312 */         setSlots(entity, drops, startSlot, count, usedItems);
/* 313 */         player.containerMenu.broadcastChanges(); continue; }
/*     */       
/* 315 */       setSlots(entity, drops, startSlot, count, usedItems);
/*     */     } 
/*     */ 
/*     */     
/* 319 */     callback.accept(usedItems);
/* 320 */     return usedItems.size();
/*     */   }
/*     */   
/*     */   private static int dropInWorld(CommandSourceStack source, Vec3 pos, List<ItemStack> drops, Callback callback) throws CommandSyntaxException {
/* 324 */     ServerLevel level = source.getLevel();
/* 325 */     drops.forEach(drop -> {
/* 326 */           ItemEntity entity = new ItemEntity(level, pos.x, pos.y, pos.z, drop.copy());
/* 327 */           entity.setDefaultPickUpDelay();
/* 328 */           level.addFreshEntity(entity);
/*     */         });
/*     */     
/* 331 */     callback.accept(drops);
/* 332 */     return drops.size();
/*     */   }
/*     */   
/*     */   private static void callback(CommandSourceStack source, List<ItemStack> drops) {
/* 336 */     if (drops.size() == 1) {
/* 337 */       ItemStack drop = (ItemStack)drops.get(0);
/* 338 */       source.sendSuccess(() -> Component.translatable("commands.drop.success.single", new Object[] { Integer.valueOf(drop.getCount()), drop.getDisplayName() }), false);
/*     */     } else {
/* 340 */       source.sendSuccess(() -> Component.translatable("commands.drop.success.multiple", new Object[] { Integer.valueOf(drops.size()) }), false);
/*     */     } 
/*     */   }
/*     */   
/*     */   private static void callback(CommandSourceStack source, List<ItemStack> drops, ResourceKey<LootTable> location) {
/* 345 */     if (drops.size() == 1) {
/* 346 */       ItemStack drop = (ItemStack)drops.get(0);
/* 347 */       source.sendSuccess(() -> Component.translatable("commands.drop.success.single_with_table", new Object[] { Integer.valueOf(drop.getCount()), drop.getDisplayName(), Component.translationArg(location.identifier()) }), false);
/*     */     } else {
/* 349 */       source.sendSuccess(() -> Component.translatable("commands.drop.success.multiple_with_table", new Object[] { Integer.valueOf(drops.size()), Component.translationArg(location.identifier()) }), false);
/*     */     } 
/*     */   }
/*     */   
/*     */   private static ItemStack getSourceHandItem(CommandSourceStack source, EquipmentSlot slot) throws CommandSyntaxException {
/* 354 */     Entity entity = source.getEntityOrException();
/* 355 */     if (entity instanceof LivingEntity) {
/* 356 */       return ((LivingEntity)entity).getItemBySlot(slot);
/*     */     }
/* 358 */     throw ERROR_NO_HELD_ITEMS.create(entity.getDisplayName());
/*     */   }
/*     */ 
/*     */   
/*     */   private static int dropBlockLoot(CommandContext<CommandSourceStack> context, BlockPos pos, ItemStack tool, DropConsumer output) throws CommandSyntaxException {
/* 363 */     CommandSourceStack source = (CommandSourceStack)context.getSource();
/* 364 */     ServerLevel level = source.getLevel();
/* 365 */     BlockState blockState = level.getBlockState(pos);
/* 366 */     BlockEntity blockEntity = level.getBlockEntity(pos);
/*     */     
/* 368 */     Optional<ResourceKey<LootTable>> lootTable = blockState.getBlock().getLootTable();
/* 369 */     if (lootTable.isEmpty()) {
/* 370 */       throw ERROR_NO_BLOCK_LOOT_TABLE.create(blockState.getBlock().getName());
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 378 */     LootParams.Builder lootParams = (new LootParams.Builder(level)).withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(pos)).withParameter(LootContextParams.BLOCK_STATE, blockState).withOptionalParameter(LootContextParams.BLOCK_ENTITY, blockEntity).withOptionalParameter(LootContextParams.THIS_ENTITY, source.getEntity()).withParameter(LootContextParams.TOOL, tool);
/*     */     
/* 380 */     List<ItemStack> drops = blockState.getDrops(lootParams);
/* 381 */     return output.accept(context, drops, usedItems -> callback(source, usedItems, (ResourceKey)lootTable.get()));
/*     */   }
/*     */   
/*     */   private static int dropKillLoot(CommandContext<CommandSourceStack> context, Entity target, DropConsumer output) throws CommandSyntaxException {
/* 385 */     Optional<ResourceKey<LootTable>> lootTableId = target.getLootTable();
/* 386 */     if (lootTableId.isEmpty()) {
/* 387 */       throw ERROR_NO_ENTITY_LOOT_TABLE.create(target.getDisplayName());
/*     */     }
/* 389 */     CommandSourceStack source = (CommandSourceStack)context.getSource();
/*     */     
/* 391 */     LootParams.Builder builder = new LootParams.Builder(source.getLevel());
/* 392 */     Entity killer = source.getEntity();
/* 393 */     if (killer instanceof Player) { Player player = (Player)killer;
/* 394 */       builder.withParameter(LootContextParams.LAST_DAMAGE_PLAYER, player); }
/*     */     
/* 396 */     builder.withParameter(LootContextParams.DAMAGE_SOURCE, target.damageSources().magic());
/* 397 */     builder.withOptionalParameter(LootContextParams.DIRECT_ATTACKING_ENTITY, killer);
/* 398 */     builder.withOptionalParameter(LootContextParams.ATTACKING_ENTITY, killer);
/* 399 */     builder.withParameter(LootContextParams.THIS_ENTITY, target);
/* 400 */     builder.withParameter(LootContextParams.ORIGIN, source.getPosition());
/* 401 */     LootParams lootParams = builder.create(LootContextParamSets.ENTITY);
/*     */     
/* 403 */     LootTable lootTable = source.getServer().reloadableRegistries().getLootTable((ResourceKey)lootTableId.get());
/* 404 */     ObjectArrayList objectArrayList = lootTable.getRandomItems(lootParams);
/* 405 */     return output.accept(context, objectArrayList, usedItems -> callback(source, usedItems, (ResourceKey)lootTableId.get()));
/*     */   }
/*     */   
/*     */   private static int dropChestLoot(CommandContext<CommandSourceStack> context, Holder<LootTable> lootTable, DropConsumer output) throws CommandSyntaxException {
/* 409 */     CommandSourceStack source = (CommandSourceStack)context.getSource();
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 414 */     LootParams lootParams = (new LootParams.Builder(source.getLevel())).withOptionalParameter(LootContextParams.THIS_ENTITY, source.getEntity()).withParameter(LootContextParams.ORIGIN, source.getPosition()).create(LootContextParamSets.CHEST);
/*     */     
/* 416 */     return drop(context, lootTable, lootParams, output);
/*     */   }
/*     */   
/*     */   private static int dropFishingLoot(CommandContext<CommandSourceStack> context, Holder<LootTable> lootTable, BlockPos pos, ItemStack tool, DropConsumer output) throws CommandSyntaxException {
/* 420 */     CommandSourceStack source = (CommandSourceStack)context.getSource();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 426 */     LootParams lootParams = (new LootParams.Builder(source.getLevel())).withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(pos)).withParameter(LootContextParams.TOOL, tool).withOptionalParameter(LootContextParams.THIS_ENTITY, source.getEntity()).create(LootContextParamSets.FISHING);
/*     */     
/* 428 */     return drop(context, lootTable, lootParams, output);
/*     */   }
/*     */   
/*     */   private static int drop(CommandContext<CommandSourceStack> context, Holder<LootTable> lootTable, LootParams lootParams, DropConsumer output) throws CommandSyntaxException {
/* 432 */     CommandSourceStack source = (CommandSourceStack)context.getSource();
/* 433 */     ObjectArrayList objectArrayList = ((LootTable)lootTable.value()).getRandomItems(lootParams);
/* 434 */     return output.accept(context, objectArrayList, usedItems -> callback(source, usedItems));
/*     */   }
/*     */   
/*     */   @FunctionalInterface
/*     */   private static interface TailProvider {
/*     */     ArgumentBuilder<CommandSourceStack, ?> construct(ArgumentBuilder<CommandSourceStack, ?> param1ArgumentBuilder, LootCommand.DropConsumer param1DropConsumer);
/*     */   }
/*     */   
/*     */   @FunctionalInterface
/*     */   private static interface DropConsumer {
/*     */     int accept(CommandContext<CommandSourceStack> param1CommandContext, List<ItemStack> param1List, LootCommand.Callback param1Callback) throws CommandSyntaxException;
/*     */   }
/*     */   
/*     */   @FunctionalInterface
/*     */   private static interface Callback {
/*     */     void accept(List<ItemStack> param1List) throws CommandSyntaxException;
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\commands\LootCommand.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */