/*     */ package net.minecraft.world.scores;
/*     */ import com.google.common.collect.Sets;
/*     */ import com.mojang.datafixers.util.Function11;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.ChatFormatting;
/*     */ import net.minecraft.network.chat.CommonComponents;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.ComponentSerialization;
/*     */ import net.minecraft.network.chat.ComponentUtils;
/*     */ import net.minecraft.network.chat.HoverEvent;
/*     */ import net.minecraft.network.chat.MutableComponent;
/*     */ import net.minecraft.network.chat.Style;
/*     */ 
/*     */ public class PlayerTeam extends Team {
/*     */   private static final int BIT_FRIENDLY_FIRE = 0;
/*     */   private static final int BIT_SEE_INVISIBLES = 1;
/*     */   private final Scoreboard scoreboard;
/*     */   private final String name;
/*     */   private final Set<String> players;
/*     */   private Component displayName;
/*     */   private Component playerPrefix;
/*     */   
/*     */   public PlayerTeam(Scoreboard scoreboard, String name) {
/*  27 */     this.players = Sets.newHashSet();
/*     */     
/*  29 */     this.playerPrefix = CommonComponents.EMPTY;
/*  30 */     this.playerSuffix = CommonComponents.EMPTY;
/*  31 */     this.allowFriendlyFire = true;
/*  32 */     this.seeFriendlyInvisibles = true;
/*  33 */     this.nameTagVisibility = Team.Visibility.ALWAYS;
/*  34 */     this.deathMessageVisibility = Team.Visibility.ALWAYS;
/*  35 */     this.color = ChatFormatting.RESET;
/*  36 */     this.collisionRule = Team.CollisionRule.ALWAYS;
/*     */ 
/*     */ 
/*     */     
/*  40 */     this.scoreboard = scoreboard;
/*  41 */     this.name = name;
/*  42 */     this.displayName = Component.literal(name);
/*     */     
/*  44 */     this
/*     */       
/*  46 */       .displayNameStyle = Style.EMPTY.withInsertion(name).withHoverEvent(new HoverEvent.ShowText(Component.literal(name)));
/*     */   }
/*     */   private Component playerSuffix; private boolean allowFriendlyFire; private boolean seeFriendlyInvisibles; private Team.Visibility nameTagVisibility; private Team.Visibility deathMessageVisibility; private ChatFormatting color; private Team.CollisionRule collisionRule; private final Style displayNameStyle;
/*     */   public Packed pack() {
/*  50 */     return new Packed(this.name, 
/*     */         
/*  52 */         Optional.of(this.displayName), 
/*  53 */         (this.color != ChatFormatting.RESET) ? Optional.of(this.color) : Optional.empty(), this.allowFriendlyFire, this.seeFriendlyInvisibles, this.playerPrefix, this.playerSuffix, this.nameTagVisibility, this.deathMessageVisibility, this.collisionRule, 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*  61 */         List.copyOf(this.players));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  66 */   public Scoreboard getScoreboard() { return this.scoreboard; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  71 */   public String getName() { return this.name; }
/*     */ 
/*     */ 
/*     */   
/*  75 */   public Component getDisplayName() { return this.displayName; }
/*     */ 
/*     */   
/*     */   public MutableComponent getFormattedDisplayName() {
/*  79 */     MutableComponent result = ComponentUtils.wrapInSquareBrackets(this.displayName.copy().withStyle(this.displayNameStyle));
/*     */     
/*  81 */     ChatFormatting color = getColor();
/*  82 */     if (color != ChatFormatting.RESET) {
/*  83 */       result.withStyle(color);
/*     */     }
/*     */     
/*  86 */     return result;
/*     */   }
/*     */   
/*     */   public void setDisplayName(Component displayName) {
/*  90 */     if (displayName == null) {
/*  91 */       throw new IllegalArgumentException("Name cannot be null");
/*     */     }
/*  93 */     this.displayName = displayName;
/*  94 */     this.scoreboard.onTeamChanged(this);
/*     */   }
/*     */   
/*     */   public void setPlayerPrefix(Component playerPrefix) {
/*  98 */     this.playerPrefix = (playerPrefix == null) ? CommonComponents.EMPTY : playerPrefix;
/*  99 */     this.scoreboard.onTeamChanged(this);
/*     */   }
/*     */ 
/*     */   
/* 103 */   public Component getPlayerPrefix() { return this.playerPrefix; }
/*     */ 
/*     */   
/*     */   public void setPlayerSuffix(Component playerSuffix) {
/* 107 */     this.playerSuffix = (playerSuffix == null) ? CommonComponents.EMPTY : playerSuffix;
/* 108 */     this.scoreboard.onTeamChanged(this);
/*     */   }
/*     */ 
/*     */   
/* 112 */   public Component getPlayerSuffix() { return this.playerSuffix; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 117 */   public Collection<String> getPlayers() { return this.players; }
/*     */ 
/*     */ 
/*     */   
/*     */   public MutableComponent getFormattedName(Component teamMemberName) {
/* 122 */     MutableComponent result = Component.empty().append(this.playerPrefix).append(teamMemberName).append(this.playerSuffix);
/*     */     
/* 124 */     ChatFormatting color = getColor();
/* 125 */     if (color != ChatFormatting.RESET) {
/* 126 */       result.withStyle(color);
/*     */     }
/*     */     
/* 129 */     return result;
/*     */   }
/*     */   
/*     */   public static MutableComponent formatNameForTeam(Team team, Component name) {
/* 133 */     if (team == null) {
/* 134 */       return name.copy();
/*     */     }
/* 136 */     return team.getFormattedName(name);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 141 */   public boolean isAllowFriendlyFire() { return this.allowFriendlyFire; }
/*     */ 
/*     */   
/*     */   public void setAllowFriendlyFire(boolean allowFriendlyFire) {
/* 145 */     this.allowFriendlyFire = allowFriendlyFire;
/* 146 */     this.scoreboard.onTeamChanged(this);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 151 */   public boolean canSeeFriendlyInvisibles() { return this.seeFriendlyInvisibles; }
/*     */ 
/*     */   
/*     */   public void setSeeFriendlyInvisibles(boolean seeFriendlyInvisibles) {
/* 155 */     this.seeFriendlyInvisibles = seeFriendlyInvisibles;
/* 156 */     this.scoreboard.onTeamChanged(this);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 161 */   public Team.Visibility getNameTagVisibility() { return this.nameTagVisibility; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 166 */   public Team.Visibility getDeathMessageVisibility() { return this.deathMessageVisibility; }
/*     */ 
/*     */   
/*     */   public void setNameTagVisibility(Team.Visibility visibility) {
/* 170 */     this.nameTagVisibility = visibility;
/* 171 */     this.scoreboard.onTeamChanged(this);
/*     */   }
/*     */   
/*     */   public void setDeathMessageVisibility(Team.Visibility visibility) {
/* 175 */     this.deathMessageVisibility = visibility;
/* 176 */     this.scoreboard.onTeamChanged(this);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 181 */   public Team.CollisionRule getCollisionRule() { return this.collisionRule; }
/*     */ 
/*     */   
/*     */   public void setCollisionRule(Team.CollisionRule collisionRule) {
/* 185 */     this.collisionRule = collisionRule;
/* 186 */     this.scoreboard.onTeamChanged(this);
/*     */   }
/*     */   
/*     */   public int packOptions() {
/* 190 */     int result = 0;
/*     */     
/* 192 */     if (isAllowFriendlyFire()) {
/* 193 */       result |= 0x1;
/*     */     }
/* 195 */     if (canSeeFriendlyInvisibles()) {
/* 196 */       result |= 0x2;
/*     */     }
/*     */     
/* 199 */     return result;
/*     */   }
/*     */   
/*     */   public void unpackOptions(int options) {
/* 203 */     setAllowFriendlyFire(((options & true) > 0));
/* 204 */     setSeeFriendlyInvisibles(((options & 0x2) > 0));
/*     */   }
/*     */   
/*     */   public void setColor(ChatFormatting color) {
/* 208 */     this.color = color;
/* 209 */     this.scoreboard.onTeamChanged(this);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 214 */   public ChatFormatting getColor() { return this.color; }
/*     */   public static final class Packed extends Record { private final String name; private final Optional<Component> displayName; private final Optional<ChatFormatting> color; private final boolean allowFriendlyFire; private final boolean seeFriendlyInvisibles; private final Component memberNamePrefix; private final Component memberNameSuffix; private final Team.Visibility nameTagVisibility; private final Team.Visibility deathMessageVisibility; private final Team.CollisionRule collisionRule; private final List<String> players;
/*     */     
/* 217 */     public Packed(String name, Optional<Component> displayName, Optional<ChatFormatting> color, boolean allowFriendlyFire, boolean seeFriendlyInvisibles, Component memberNamePrefix, Component memberNameSuffix, Team.Visibility nameTagVisibility, Team.Visibility deathMessageVisibility, Team.CollisionRule collisionRule, List<String> players) { this.name = name; this.displayName = displayName; this.color = color; this.allowFriendlyFire = allowFriendlyFire; this.seeFriendlyInvisibles = seeFriendlyInvisibles; this.memberNamePrefix = memberNamePrefix; this.memberNameSuffix = memberNameSuffix; this.nameTagVisibility = nameTagVisibility; this.deathMessageVisibility = deathMessageVisibility; this.collisionRule = collisionRule; this.players = players; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/scores/PlayerTeam$Packed;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #217	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/scores/PlayerTeam$Packed; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/scores/PlayerTeam$Packed;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #217	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/scores/PlayerTeam$Packed; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/scores/PlayerTeam$Packed;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #217	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/scores/PlayerTeam$Packed;
/* 217 */       //   0	8	1	o	Ljava/lang/Object; } public String name() { return this.name; } public Optional<Component> displayName() { return this.displayName; } public Optional<ChatFormatting> color() { return this.color; } public boolean allowFriendlyFire() { return this.allowFriendlyFire; } public boolean seeFriendlyInvisibles() { return this.seeFriendlyInvisibles; } public Component memberNamePrefix() { return this.memberNamePrefix; } public Component memberNameSuffix() { return this.memberNameSuffix; } public Team.Visibility nameTagVisibility() { return this.nameTagVisibility; } public Team.Visibility deathMessageVisibility() { return this.deathMessageVisibility; } public Team.CollisionRule collisionRule() { return this.collisionRule; } public List<String> players() { return this.players; }
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
/* 230 */     public static final Codec<Packed> CODEC = RecordCodecBuilder.create(i -> i.group(Codec.STRING
/* 231 */           .fieldOf("Name").forGetter(Packed::name), ComponentSerialization.CODEC
/* 232 */           .optionalFieldOf("DisplayName").forGetter(Packed::displayName), ChatFormatting.COLOR_CODEC
/* 233 */           .optionalFieldOf("TeamColor").forGetter(Packed::color), Codec.BOOL
/* 234 */           .optionalFieldOf("AllowFriendlyFire", Boolean.valueOf(true)).forGetter(Packed::allowFriendlyFire), Codec.BOOL
/* 235 */           .optionalFieldOf("SeeFriendlyInvisibles", Boolean.valueOf(true)).forGetter(Packed::seeFriendlyInvisibles), ComponentSerialization.CODEC
/* 236 */           .optionalFieldOf("MemberNamePrefix", CommonComponents.EMPTY).forGetter(Packed::memberNamePrefix), ComponentSerialization.CODEC
/* 237 */           .optionalFieldOf("MemberNameSuffix", CommonComponents.EMPTY).forGetter(Packed::memberNameSuffix), Team.Visibility.CODEC
/* 238 */           .optionalFieldOf("NameTagVisibility", Team.Visibility.ALWAYS).forGetter(Packed::nameTagVisibility), Team.Visibility.CODEC
/* 239 */           .optionalFieldOf("DeathMessageVisibility", Team.Visibility.ALWAYS).forGetter(Packed::deathMessageVisibility), Team.CollisionRule.CODEC
/* 240 */           .optionalFieldOf("CollisionRule", Team.CollisionRule.ALWAYS).forGetter(Packed::collisionRule), Codec.STRING
/* 241 */           .listOf().optionalFieldOf("Players", List.of()).forGetter(Packed::players))
/* 242 */         .apply(i, Packed::new)); }
/*     */ 
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\scores\PlayerTeam.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */