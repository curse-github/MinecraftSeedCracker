/*     */ package net.minecraft.server.commands;
/*     */ 
/*     */ import com.google.common.collect.Maps;
/*     */ import com.google.common.collect.Sets;
/*     */ import com.mojang.brigadier.CommandDispatcher;
/*     */ import com.mojang.brigadier.Message;
/*     */ import com.mojang.brigadier.arguments.BoolArgumentType;
/*     */ import com.mojang.brigadier.arguments.FloatArgumentType;
/*     */ import com.mojang.brigadier.arguments.IntegerArgumentType;
/*     */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*     */ import com.mojang.brigadier.builder.RequiredArgumentBuilder;
/*     */ import com.mojang.brigadier.context.CommandContext;
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import com.mojang.brigadier.exceptions.Dynamic2CommandExceptionType;
/*     */ import com.mojang.brigadier.exceptions.Dynamic4CommandExceptionType;
/*     */ import java.util.Collection;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import net.minecraft.commands.CommandSourceStack;
/*     */ import net.minecraft.commands.Commands;
/*     */ import net.minecraft.commands.arguments.EntityArgument;
/*     */ import net.minecraft.commands.arguments.coordinates.Vec2Argument;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.phys.Vec2;
/*     */ import net.minecraft.world.scores.PlayerTeam;
/*     */ import net.minecraft.world.scores.Team;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SpreadPlayersCommand
/*     */ {
/*     */   private static final int MAX_ITERATION_COUNT = 10000;
/*  45 */   private static final Dynamic4CommandExceptionType ERROR_FAILED_TO_SPREAD_TEAMS = new Dynamic4CommandExceptionType((count, x, z, recommended) -> Component.translatableEscape("commands.spreadplayers.failed.teams", new Object[] { count, x, z, recommended }));
/*  46 */   private static final Dynamic4CommandExceptionType ERROR_FAILED_TO_SPREAD_ENTITIES = new Dynamic4CommandExceptionType((count, x, z, recommended) -> Component.translatableEscape("commands.spreadplayers.failed.entities", new Object[] { count, x, z, recommended }));
/*  47 */   private static final Dynamic2CommandExceptionType ERROR_INVALID_MAX_HEIGHT = new Dynamic2CommandExceptionType((suppliedMaxHeight, worldMinHeight) -> Component.translatableEscape("commands.spreadplayers.failed.invalid.height", new Object[] { suppliedMaxHeight, worldMinHeight }));
/*     */   
/*     */   public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
/*  50 */     dispatcher.register(
/*  51 */         (LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("spreadplayers")
/*  52 */         .requires(Commands.hasPermission(Commands.LEVEL_GAMEMASTERS)))
/*  53 */         .then(
/*  54 */           Commands.argument("center", Vec2Argument.vec2())
/*  55 */           .then(
/*  56 */             Commands.argument("spreadDistance", FloatArgumentType.floatArg(0.0F))
/*  57 */             .then((
/*  58 */               (RequiredArgumentBuilder)Commands.argument("maxRange", FloatArgumentType.floatArg(1.0F))
/*  59 */               .then(
/*  60 */                 Commands.argument("respectTeams", BoolArgumentType.bool())
/*  61 */                 .then(
/*  62 */                   Commands.argument("targets", EntityArgument.entities())
/*  63 */                   .executes(c -> spreadPlayers((CommandSourceStack)c.getSource(), Vec2Argument.getVec2(c, "center"), FloatArgumentType.getFloat(c, "spreadDistance"), FloatArgumentType.getFloat(c, "maxRange"), ((CommandSourceStack)c.getSource()).getLevel().getMaxY() + 1, BoolArgumentType.getBool(c, "respectTeams"), EntityArgument.getEntities(c, "targets"))))))
/*     */ 
/*     */               
/*  66 */               .then(
/*  67 */                 Commands.literal("under")
/*  68 */                 .then(
/*  69 */                   Commands.argument("maxHeight", IntegerArgumentType.integer())
/*  70 */                   .then(
/*  71 */                     Commands.argument("respectTeams", BoolArgumentType.bool())
/*  72 */                     .then(
/*  73 */                       Commands.argument("targets", EntityArgument.entities())
/*  74 */                       .executes(c -> spreadPlayers((CommandSourceStack)c.getSource(), Vec2Argument.getVec2(c, "center"), FloatArgumentType.getFloat(c, "spreadDistance"), FloatArgumentType.getFloat(c, "maxRange"), IntegerArgumentType.getInteger(c, "maxHeight"), BoolArgumentType.getBool(c, "respectTeams"), EntityArgument.getEntities(c, "targets")))))))))));
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
/*     */   private static int spreadPlayers(CommandSourceStack source, Vec2 center, float spreadDistance, float maxRange, int maxHeight, boolean respectTeams, Collection<? extends Entity> entities) throws CommandSyntaxException {
/*  86 */     ServerLevel level = source.getLevel();
/*  87 */     int minY = level.getMinY();
/*  88 */     if (maxHeight < minY) {
/*  89 */       throw ERROR_INVALID_MAX_HEIGHT.create(Integer.valueOf(maxHeight), Integer.valueOf(minY));
/*     */     }
/*     */     
/*  92 */     RandomSource random = RandomSource.create();
/*  93 */     double minX = (center.x - maxRange);
/*  94 */     double minZ = (center.y - maxRange);
/*  95 */     double maxX = (center.x + maxRange);
/*  96 */     double maxZ = (center.y + maxRange);
/*     */     
/*  98 */     Position[] arrayOfPosition = createInitialPositions(random, respectTeams ? getNumberOfTeams(entities) : entities.size(), minX, minZ, maxX, maxZ);
/*  99 */     spreadPositions(center, spreadDistance, level, random, minX, minZ, maxX, maxZ, maxHeight, arrayOfPosition, respectTeams);
/* 100 */     double distance = setPlayerPositions(entities, level, arrayOfPosition, maxHeight, respectTeams);
/*     */     
/* 102 */     source.sendSuccess(() -> Component.translatable("commands.spreadplayers.success." + (respectTeams ? "teams" : "entities"), new Object[] { Integer.valueOf(positions.length), Float.valueOf(center.x), Float.valueOf(center.y), String.format(Locale.ROOT, "%.2f", new Object[] { Double.valueOf(distance) }) }), true);
/* 103 */     return arrayOfPosition.length;
/*     */   }
/*     */   
/*     */   private static int getNumberOfTeams(Collection<? extends Entity> players) {
/* 107 */     Set<Team> teams = Sets.newHashSet();
/*     */     
/* 109 */     for (Entity player : players) {
/* 110 */       if (player instanceof net.minecraft.world.entity.player.Player) {
/* 111 */         teams.add(player.getTeam()); continue;
/*     */       } 
/* 113 */       teams.add(null);
/*     */     } 
/*     */ 
/*     */     
/* 117 */     return teams.size();
/*     */   }
/*     */   
/*     */   private static void spreadPositions(Vec2 center, double spreadDist, ServerLevel level, RandomSource random, double minX, double minZ, double maxX, double maxZ, int maxHeight, Position[] positions, boolean respectTeams) throws CommandSyntaxException {
/* 121 */     boolean hasCollisions = true;
/*     */     
/* 123 */     double minDistance = 3.4028234663852886E38D;
/*     */     int iteration;
/* 125 */     for (iteration = 0; iteration < 10000 && hasCollisions; iteration++) {
/* 126 */       hasCollisions = false;
/* 127 */       minDistance = 3.4028234663852886E38D;
/*     */       
/* 129 */       for (int i = 0; i < positions.length; i++) {
/* 130 */         Position position = positions[i];
/* 131 */         int neighbourCount = 0;
/* 132 */         Position averageNeighbourPos = new Position();
/*     */         
/* 134 */         for (int j = 0; j < positions.length; j++) {
/* 135 */           if (i != j) {
/*     */ 
/*     */             
/* 138 */             Position neighbour = positions[j];
/*     */             
/* 140 */             double dist = position.dist(neighbour);
/* 141 */             minDistance = Math.min(dist, minDistance);
/* 142 */             if (dist < spreadDist) {
/* 143 */               neighbourCount++;
/* 144 */               averageNeighbourPos.x += neighbour.x - position.x;
/* 145 */               averageNeighbourPos.z += neighbour.z - position.z;
/*     */             } 
/*     */           } 
/*     */         } 
/* 149 */         if (neighbourCount > 0) {
/* 150 */           averageNeighbourPos.x /= neighbourCount;
/* 151 */           averageNeighbourPos.z /= neighbourCount;
/* 152 */           double length = averageNeighbourPos.getLength();
/*     */           
/* 154 */           if (length > 0.0D) {
/* 155 */             averageNeighbourPos.normalize();
/*     */             
/* 157 */             position.moveAway(averageNeighbourPos);
/*     */           } else {
/* 159 */             position.randomize(random, minX, minZ, maxX, maxZ);
/*     */           } 
/*     */           
/* 162 */           hasCollisions = true;
/*     */         } 
/*     */         
/* 165 */         if (position.clamp(minX, minZ, maxX, maxZ)) {
/* 166 */           hasCollisions = true;
/*     */         }
/*     */       } 
/*     */       
/* 170 */       if (!hasCollisions) {
/* 171 */         for (Position position : positions) {
/* 172 */           if (!position.isSafe(level, maxHeight)) {
/* 173 */             position.randomize(random, minX, minZ, maxX, maxZ);
/* 174 */             hasCollisions = true;
/*     */           } 
/*     */         } 
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 181 */     if (minDistance == 3.4028234663852886E38D) {
/* 182 */       minDistance = 0.0D;
/*     */     }
/*     */     
/* 185 */     if (iteration >= 10000) {
/* 186 */       if (respectTeams) {
/* 187 */         throw ERROR_FAILED_TO_SPREAD_TEAMS.create(Integer.valueOf(positions.length), Float.valueOf(center.x), Float.valueOf(center.y), String.format(Locale.ROOT, "%.2f", new Object[] { Double.valueOf(minDistance) }));
/*     */       }
/* 189 */       throw ERROR_FAILED_TO_SPREAD_ENTITIES.create(Integer.valueOf(positions.length), Float.valueOf(center.x), Float.valueOf(center.y), String.format(Locale.ROOT, "%.2f", new Object[] { Double.valueOf(minDistance) }));
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private static double setPlayerPositions(Collection<? extends Entity> entities, ServerLevel level, Position[] positions, int maxHeight, boolean respectTeams) {
/* 195 */     avgDistance = 0.0D;
/* 196 */     int positionIndex = 0;
/* 197 */     Map<Team, Position> teamPositions = Maps.newHashMap();
/*     */     
/* 199 */     for (Entity entity : entities) {
/*     */       Position position;
/*     */       
/* 202 */       if (respectTeams) {
/* 203 */         PlayerTeam playerTeam = (entity instanceof net.minecraft.world.entity.player.Player) ? entity.getTeam() : null;
/*     */         
/* 205 */         if (!teamPositions.containsKey(playerTeam)) {
/* 206 */           teamPositions.put(playerTeam, positions[positionIndex++]);
/*     */         }
/*     */         
/* 209 */         position = (Position)teamPositions.get(playerTeam);
/*     */       } else {
/* 211 */         position = positions[positionIndex++];
/*     */       } 
/*     */       
/* 214 */       entity.teleportTo(level, Mth.floor(position.x) + 0.5D, position.getSpawnY(level, maxHeight), Mth.floor(position.z) + 0.5D, Set.of(), entity.getYRot(), entity.getXRot(), true);
/*     */       
/* 216 */       double closest = Double.MAX_VALUE;
/* 217 */       for (Position testPosition : positions) {
/* 218 */         if (position != testPosition) {
/*     */ 
/*     */ 
/*     */           
/* 222 */           double dist = position.dist(testPosition);
/* 223 */           closest = Math.min(dist, closest);
/*     */         } 
/* 225 */       }  avgDistance += closest;
/*     */     } 
/* 227 */     if (entities.size() < 2) {
/* 228 */       return 0.0D;
/*     */     }
/*     */     
/* 231 */     return entities.size();
/*     */   }
/*     */ 
/*     */   
/*     */   private static Position[] createInitialPositions(RandomSource random, int count, double minX, double minZ, double maxX, double maxZ) {
/* 236 */     Position[] arrayOfPosition = new Position[count];
/*     */     
/* 238 */     for (int i = 0; i < arrayOfPosition.length; i++) {
/* 239 */       Position position = new Position();
/* 240 */       position.randomize(random, minX, minZ, maxX, maxZ);
/* 241 */       arrayOfPosition[i] = position;
/*     */     } 
/*     */     
/* 244 */     return arrayOfPosition;
/*     */   }
/*     */   
/*     */   private static class Position {
/*     */     private double x;
/*     */     private double z;
/*     */     
/*     */     double dist(Position target) {
/* 252 */       double dx = this.x - target.x;
/* 253 */       double dz = this.z - target.z;
/*     */       
/* 255 */       return Math.sqrt(dx * dx + dz * dz);
/*     */     }
/*     */     
/*     */     void normalize() {
/* 259 */       double dist = getLength();
/* 260 */       this.x /= dist;
/* 261 */       this.z /= dist;
/*     */     }
/*     */ 
/*     */     
/* 265 */     double getLength() { return Math.sqrt(this.x * this.x + this.z * this.z); }
/*     */ 
/*     */     
/*     */     public void moveAway(Position pos) {
/* 269 */       this.x -= pos.x;
/* 270 */       this.z -= pos.z;
/*     */     }
/*     */     
/*     */     public boolean clamp(double minX, double minZ, double maxX, double maxZ) {
/* 274 */       boolean changed = false;
/*     */       
/* 276 */       if (this.x < minX) {
/* 277 */         this.x = minX;
/* 278 */         changed = true;
/* 279 */       } else if (this.x > maxX) {
/* 280 */         this.x = maxX;
/* 281 */         changed = true;
/*     */       } 
/*     */       
/* 284 */       if (this.z < minZ) {
/* 285 */         this.z = minZ;
/* 286 */         changed = true;
/* 287 */       } else if (this.z > maxZ) {
/* 288 */         this.z = maxZ;
/* 289 */         changed = true;
/*     */       } 
/*     */       
/* 292 */       return changed;
/*     */     }
/*     */     
/*     */     public int getSpawnY(BlockGetter level, int maxHeight) {
/* 296 */       BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos(this.x, (maxHeight + 1), this.z);
/* 297 */       boolean air2Above = level.getBlockState(pos).isAir();
/* 298 */       pos.move(Direction.DOWN);
/* 299 */       boolean air1Above = level.getBlockState(pos).isAir();
/* 300 */       while (pos.getY() > level.getMinY()) {
/* 301 */         pos.move(Direction.DOWN);
/* 302 */         boolean currentIsAir = level.getBlockState(pos).isAir();
/*     */         
/* 304 */         if (!currentIsAir && air1Above && air2Above) {
/* 305 */           return pos.getY() + 1;
/*     */         }
/* 307 */         air2Above = air1Above;
/* 308 */         air1Above = currentIsAir;
/*     */       } 
/*     */       
/* 311 */       return maxHeight + 1;
/*     */     }
/*     */     
/*     */     public boolean isSafe(BlockGetter level, int maxHeight) {
/* 315 */       BlockPos pos = BlockPos.containing(this.x, (getSpawnY(level, maxHeight) - 1), this.z);
/* 316 */       BlockState state = level.getBlockState(pos);
/* 317 */       return (pos.getY() < maxHeight && !state.liquid() && !state.is(BlockTags.FIRE));
/*     */     }
/*     */     
/*     */     public void randomize(RandomSource random, double minX, double minZ, double maxX, double maxZ) {
/* 321 */       this.x = Mth.nextDouble(random, minX, maxX);
/* 322 */       this.z = Mth.nextDouble(random, minZ, maxZ);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\commands\SpreadPlayersCommand.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */