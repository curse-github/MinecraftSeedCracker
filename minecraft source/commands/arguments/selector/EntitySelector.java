/*     */ package net.minecraft.commands.arguments.selector;
/*     */ 
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectArrayList;
/*     */ import java.util.List;
/*     */ import java.util.UUID;
/*     */ import java.util.function.BiConsumer;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.Predicate;
/*     */ import net.minecraft.advancements.criterion.MinMaxBounds;
/*     */ import net.minecraft.commands.CommandSourceStack;
/*     */ import net.minecraft.commands.arguments.EntityArgument;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.ComponentUtils;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.server.permissions.Permissions;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.flag.FeatureFlagSet;
/*     */ import net.minecraft.world.level.entity.EntityTypeTest;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class EntitySelector {
/*     */   public static final int INFINITE = 2147483647;
/*     */   public static final BiConsumer<Vec3, List<? extends Entity>> ORDER_ARBITRARY = (p, c) -> {
/*     */     
/*     */     };
/*     */   
/*  32 */   private static final EntityTypeTest<Entity, ?> ANY_TYPE = new EntityTypeTest<Entity, Entity>()
/*     */     {
/*     */       public Entity tryCast(Entity entity) {
/*  35 */         return entity;
/*     */       }
/*     */ 
/*     */ 
/*     */       
/*  40 */       public Class<? extends Entity> getBaseClass() { return Entity.class; }
/*     */     };
/*     */ 
/*     */   
/*     */   private final int maxResults;
/*     */   
/*     */   private final boolean includesEntities;
/*     */   private final boolean worldLimited;
/*     */   private final List<Predicate<Entity>> contextFreePredicates;
/*     */   private final MinMaxBounds.Doubles range;
/*     */   private final Function<Vec3, Vec3> position;
/*     */   private final AABB aabb;
/*     */   private final BiConsumer<Vec3, List<? extends Entity>> order;
/*     */   private final boolean currentEntity;
/*     */   private final String playerName;
/*     */   private final UUID entityUUID;
/*     */   private final EntityTypeTest<Entity, ?> type;
/*     */   private final boolean usesSelector;
/*     */   
/*     */   public EntitySelector(int maxResults, boolean includesEntities, boolean worldLimited, List<Predicate<Entity>> contextFreePredicates, MinMaxBounds.Doubles range, Function<Vec3, Vec3> position, AABB aabb, BiConsumer<Vec3, List<? extends Entity>> order, boolean currentEntity, String playerName, UUID entityUUID, EntityType<?> type, boolean usesSelector) {
/*  60 */     this.maxResults = maxResults;
/*  61 */     this.includesEntities = includesEntities;
/*  62 */     this.worldLimited = worldLimited;
/*  63 */     this.contextFreePredicates = contextFreePredicates;
/*  64 */     this.range = range;
/*  65 */     this.position = position;
/*  66 */     this.aabb = aabb;
/*  67 */     this.order = order;
/*  68 */     this.currentEntity = currentEntity;
/*  69 */     this.playerName = playerName;
/*  70 */     this.entityUUID = entityUUID;
/*  71 */     this.type = (type == null) ? ANY_TYPE : type;
/*  72 */     this.usesSelector = usesSelector;
/*     */   }
/*     */ 
/*     */   
/*  76 */   public int getMaxResults() { return this.maxResults; }
/*     */ 
/*     */ 
/*     */   
/*  80 */   public boolean includesEntities() { return this.includesEntities; }
/*     */ 
/*     */ 
/*     */   
/*  84 */   public boolean isSelfSelector() { return this.currentEntity; }
/*     */ 
/*     */ 
/*     */   
/*  88 */   public boolean isWorldLimited() { return this.worldLimited; }
/*     */ 
/*     */ 
/*     */   
/*  92 */   public boolean usesSelector() { return this.usesSelector; }
/*     */ 
/*     */   
/*     */   private void checkPermissions(CommandSourceStack sender) throws CommandSyntaxException {
/*  96 */     if (this.usesSelector && !sender.permissions().hasPermission(Permissions.COMMANDS_ENTITY_SELECTORS)) {
/*  97 */       throw EntityArgument.ERROR_SELECTORS_NOT_ALLOWED.create();
/*     */     }
/*     */   }
/*     */   
/*     */   public Entity findSingleEntity(CommandSourceStack sender) throws CommandSyntaxException {
/* 102 */     checkPermissions(sender);
/*     */     
/* 104 */     List<? extends Entity> entities = findEntities(sender);
/* 105 */     if (entities.isEmpty()) {
/* 106 */       throw EntityArgument.NO_ENTITIES_FOUND.create();
/*     */     }
/* 108 */     if (entities.size() > 1) {
/* 109 */       throw EntityArgument.ERROR_NOT_SINGLE_ENTITY.create();
/*     */     }
/* 111 */     return (Entity)entities.get(0);
/*     */   }
/*     */   
/*     */   public List<? extends Entity> findEntities(CommandSourceStack sender) throws CommandSyntaxException {
/* 115 */     checkPermissions(sender);
/*     */     
/* 117 */     if (!this.includesEntities) {
/* 118 */       return findPlayers(sender);
/*     */     }
/* 120 */     if (this.playerName != null) {
/* 121 */       ServerPlayer result = sender.getServer().getPlayerList().getPlayerByName(this.playerName);
/* 122 */       if (result == null) {
/* 123 */         return List.of();
/*     */       }
/* 125 */       return List.of(result);
/*     */     } 
/* 127 */     if (this.entityUUID != null) {
/* 128 */       for (ServerLevel level : sender.getServer().getAllLevels()) {
/* 129 */         Entity entity = level.getEntity(this.entityUUID);
/* 130 */         if (entity != null) {
/* 131 */           if (!entity.getType().isEnabled(sender.enabledFeatures())) {
/*     */             break;
/*     */           }
/* 134 */           return List.of(entity);
/*     */         } 
/*     */       } 
/* 137 */       return List.of();
/*     */     } 
/* 139 */     Vec3 pos = (Vec3)this.position.apply(sender.getPosition());
/* 140 */     AABB absoluteAabb = getAbsoluteAabb(pos);
/*     */     
/* 142 */     if (this.currentEntity) {
/*     */       
/* 144 */       Predicate<Entity> predicate = getPredicate(pos, absoluteAabb, null);
/* 145 */       if (sender.getEntity() != null && predicate.test(sender.getEntity())) {
/* 146 */         return List.of(sender.getEntity());
/*     */       }
/* 148 */       return List.of();
/*     */     } 
/*     */ 
/*     */     
/* 152 */     Predicate<Entity> predicate = getPredicate(pos, absoluteAabb, sender.enabledFeatures());
/* 153 */     ObjectArrayList objectArrayList = new ObjectArrayList();
/*     */     
/* 155 */     if (isWorldLimited()) {
/* 156 */       addEntities(objectArrayList, sender.getLevel(), absoluteAabb, predicate);
/*     */     } else {
/* 158 */       for (ServerLevel level : sender.getServer().getAllLevels()) {
/* 159 */         addEntities(objectArrayList, level, absoluteAabb, predicate);
/*     */       }
/*     */     } 
/*     */     
/* 163 */     return sortAndLimit(pos, objectArrayList);
/*     */   }
/*     */   
/*     */   private void addEntities(List<Entity> result, ServerLevel level, AABB absoluteAABB, Predicate<Entity> predicate) {
/* 167 */     int limit = getResultLimit();
/* 168 */     if (result.size() >= limit) {
/*     */       return;
/*     */     }
/* 171 */     if (absoluteAABB != null) {
/* 172 */       level.getEntities(this.type, absoluteAABB, predicate, result, limit);
/*     */     } else {
/* 174 */       level.getEntities(this.type, predicate, result, limit);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 180 */   private int getResultLimit() { return (this.order == ORDER_ARBITRARY) ? this.maxResults : Integer.MAX_VALUE; }
/*     */ 
/*     */   
/*     */   public ServerPlayer findSinglePlayer(CommandSourceStack sender) throws CommandSyntaxException {
/* 184 */     checkPermissions(sender);
/*     */     
/* 186 */     List<ServerPlayer> players = findPlayers(sender);
/* 187 */     if (players.size() != 1) {
/* 188 */       throw EntityArgument.NO_PLAYERS_FOUND.create();
/*     */     }
/* 190 */     return (ServerPlayer)players.get(0);
/*     */   }
/*     */   public List<ServerPlayer> findPlayers(CommandSourceStack sender) throws CommandSyntaxException {
/*     */     ObjectArrayList objectArrayList;
/* 194 */     checkPermissions(sender);
/*     */ 
/*     */     
/* 197 */     if (this.playerName != null) {
/* 198 */       ServerPlayer result = sender.getServer().getPlayerList().getPlayerByName(this.playerName);
/* 199 */       if (result == null) {
/* 200 */         return List.of();
/*     */       }
/* 202 */       return List.of(result);
/*     */     } 
/* 204 */     if (this.entityUUID != null) {
/* 205 */       ServerPlayer result = sender.getServer().getPlayerList().getPlayer(this.entityUUID);
/* 206 */       if (result == null) {
/* 207 */         return List.of();
/*     */       }
/* 209 */       return List.of(result);
/*     */     } 
/*     */ 
/*     */     
/* 213 */     Vec3 pos = (Vec3)this.position.apply(sender.getPosition());
/* 214 */     AABB absoluteAabb = getAbsoluteAabb(pos);
/* 215 */     Predicate<Entity> predicate = getPredicate(pos, absoluteAabb, null);
/*     */     
/* 217 */     if (this.currentEntity) {
/* 218 */       Entity entity = sender.getEntity(); if (entity instanceof ServerPlayer) { objectArrayList = (ServerPlayer)entity;
/* 219 */         if (predicate.test(objectArrayList)) {
/* 220 */           return List.of(objectArrayList);
/*     */         } }
/*     */       
/* 223 */       return List.of();
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 228 */     int limit = getResultLimit();
/* 229 */     if (isWorldLimited()) {
/* 230 */       objectArrayList = sender.getLevel().getPlayers(predicate, limit);
/*     */     } else {
/* 232 */       objectArrayList = new ObjectArrayList();
/* 233 */       for (ServerPlayer player : sender.getServer().getPlayerList().getPlayers()) {
/* 234 */         if (predicate.test(player)) {
/* 235 */           objectArrayList.add(player);
/* 236 */           if (objectArrayList.size() >= limit) {
/* 237 */             return objectArrayList;
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 243 */     return sortAndLimit(pos, objectArrayList);
/*     */   }
/*     */ 
/*     */   
/* 247 */   private AABB getAbsoluteAabb(Vec3 pos) { return (this.aabb != null) ? this.aabb.move(pos) : null; }
/*     */   
/*     */   private Predicate<Entity> getPredicate(Vec3 pos, AABB absoluteAabb, FeatureFlagSet enabledFeatures) {
/*     */     ObjectArrayList objectArrayList;
/* 251 */     boolean filterFeatures = (enabledFeatures != null);
/* 252 */     boolean filterAabb = (absoluteAabb != null);
/* 253 */     boolean filterRange = (this.range != null);
/*     */     
/* 255 */     int extraCount = (filterFeatures ? 1 : 0) + (filterAabb ? 1 : 0) + (filterRange ? 1 : 0);
/*     */ 
/*     */     
/* 258 */     if (extraCount == 0) {
/* 259 */       objectArrayList = this.contextFreePredicates;
/*     */     } else {
/* 261 */       ObjectArrayList objectArrayList1 = new ObjectArrayList(this.contextFreePredicates.size() + extraCount);
/* 262 */       objectArrayList1.addAll(this.contextFreePredicates);
/*     */       
/* 264 */       if (filterFeatures) {
/* 265 */         objectArrayList1.add(e -> e.getType().isEnabled(enabledFeatures));
/*     */       }
/*     */       
/* 268 */       if (filterAabb) {
/* 269 */         objectArrayList1.add(e -> absoluteAabb.intersects(e.getBoundingBox()));
/*     */       }
/*     */       
/* 272 */       if (filterRange) {
/* 273 */         objectArrayList1.add(e -> this.range.matchesSqr(e.distanceToSqr(pos)));
/*     */       }
/* 275 */       objectArrayList = objectArrayList1;
/*     */     } 
/* 277 */     return Util.allOf(objectArrayList);
/*     */   }
/*     */   
/*     */   private <T extends Entity> List<T> sortAndLimit(Vec3 pos, List<T> result) {
/* 281 */     if (result.size() > 1) {
/* 282 */       this.order.accept(pos, result);
/*     */     }
/*     */     
/* 285 */     return result.subList(0, Math.min(this.maxResults, result.size()));
/*     */   }
/*     */ 
/*     */   
/* 289 */   public static Component joinNames(List<? extends Entity> entities) { return ComponentUtils.formatList(entities, Entity::getDisplayName); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\commands\arguments\selector\EntitySelector.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */