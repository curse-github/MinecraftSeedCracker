/*     */ package net.minecraft.world.item.component;
/*     */ 
/*     */ import com.mojang.authlib.GameProfile;
/*     */ import com.mojang.authlib.properties.PropertyMap;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.util.Either;
/*     */ import com.mojang.datafixers.util.Function3;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import java.util.Optional;
/*     */ import java.util.UUID;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.function.Consumer;
/*     */ import net.minecraft.ChatFormatting;
/*     */ import net.minecraft.core.UUIDUtil;
/*     */ import net.minecraft.core.component.DataComponentGetter;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.codec.ByteBufCodecs;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ import net.minecraft.server.players.ProfileResolver;
/*     */ import net.minecraft.util.ExtraCodecs;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.entity.player.PlayerSkin;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.TooltipFlag;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class ResolvableProfile
/*     */   implements TooltipProvider
/*     */ {
/*  35 */   private static final Codec<ResolvableProfile> FULL_CODEC = RecordCodecBuilder.create(i -> i.group(
/*     */         
/*  37 */         Codec.mapEither(ExtraCodecs.STORED_GAME_PROFILE, Partial.MAP_CODEC).forGetter(ResolvableProfile::unpack), PlayerSkin.Patch.MAP_CODEC
/*  38 */         .forGetter(ResolvableProfile::skinPatch))
/*  39 */       .apply(i, ResolvableProfile::create));
/*     */   
/*  41 */   public static final Codec<ResolvableProfile> CODEC = Codec.withAlternative(FULL_CODEC, ExtraCodecs.PLAYER_NAME, ResolvableProfile::createUnresolved);
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  46 */   public static final StreamCodec<ByteBuf, ResolvableProfile> STREAM_CODEC = StreamCodec.composite(
/*  47 */       ByteBufCodecs.either(ByteBufCodecs.GAME_PROFILE, Partial.STREAM_CODEC), ResolvableProfile::unpack, PlayerSkin.Patch.STREAM_CODEC, ResolvableProfile::skinPatch, ResolvableProfile::create);
/*     */   
/*     */   protected final GameProfile partialProfile;
/*     */   protected final PlayerSkin.Patch skinPatch;
/*     */   
/*     */   private static ResolvableProfile create(Either<GameProfile, Partial> value, PlayerSkin.Patch patch) {
/*  53 */     return (ResolvableProfile)value.map(full -> 
/*  54 */         new Static(Either.left(full), patch), partial -> {
/*     */ 
/*     */ 
/*     */ 
/*     */           
/*  59 */           if (!partial.properties.isEmpty() || partial.id.isPresent() == partial.name.isPresent()) {
/*  60 */             return new Static(Either.right(partial), patch);
/*     */           }
/*     */           
/*  63 */           return (ResolvableProfile)partial.name.map(())
/*  64 */             .orElseGet(());
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  70 */   public static ResolvableProfile createResolved(GameProfile gameProfile) { return new Static(Either.left(gameProfile), PlayerSkin.Patch.EMPTY); }
/*     */ 
/*     */ 
/*     */   
/*  74 */   public static ResolvableProfile createUnresolved(String name) { return new Dynamic(Either.left(name), PlayerSkin.Patch.EMPTY); }
/*     */ 
/*     */ 
/*     */   
/*  78 */   public static ResolvableProfile createUnresolved(UUID id) { return new Dynamic(Either.right(id), PlayerSkin.Patch.EMPTY); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected ResolvableProfile(GameProfile partialProfile, PlayerSkin.Patch skinPatch) {
/*  87 */     this.partialProfile = partialProfile;
/*  88 */     this.skinPatch = skinPatch;
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
/* 102 */   public GameProfile partialProfile() { return this.partialProfile; }
/*     */ 
/*     */ 
/*     */   
/* 106 */   public PlayerSkin.Patch skinPatch() { return this.skinPatch; }
/*     */   protected abstract Either<GameProfile, Partial> unpack();
/*     */   public abstract CompletableFuture<GameProfile> resolveProfile(ProfileResolver paramProfileResolver);
/*     */   private static GameProfile createPartialProfile(Optional<String> maybeName, Optional<UUID> maybeId, PropertyMap properties) {
/* 110 */     String name = (String)maybeName.orElse("");
/*     */     
/* 112 */     UUID id = (UUID)maybeId.orElseGet(() -> (UUID)maybeName.map(UUIDUtil::createOfflinePlayerUUID).orElse(Util.NIL_UUID));
/* 113 */     return new GameProfile(id, name, properties);
/*     */   }
/*     */   public abstract Optional<String> name();
/*     */   protected static final class Partial extends Record { private final Optional<String> name; private final Optional<UUID> id; private final PropertyMap properties;
/*     */     
/* 118 */     protected Partial(Optional<String> name, Optional<UUID> id, PropertyMap properties) { this.name = name; this.id = id; this.properties = properties; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/component/ResolvableProfile$Partial;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #118	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/* 118 */       //   0	7	0	this	Lnet/minecraft/world/item/component/ResolvableProfile$Partial; } public Optional<String> name() { return this.name; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/component/ResolvableProfile$Partial;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #118	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/item/component/ResolvableProfile$Partial; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/component/ResolvableProfile$Partial;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #118	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/item/component/ResolvableProfile$Partial;
/* 118 */       //   0	8	1	o	Ljava/lang/Object; } public Optional<UUID> id() { return this.id; } public PropertyMap properties() { return this.properties; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 123 */     public static final Partial EMPTY = new Partial(Optional.empty(), Optional.empty(), PropertyMap.EMPTY);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 128 */     private static final MapCodec<Partial> MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group(ExtraCodecs.PLAYER_NAME
/* 129 */           .optionalFieldOf("name").forGetter(Partial::name), UUIDUtil.CODEC
/* 130 */           .optionalFieldOf("id").forGetter(Partial::id), ExtraCodecs.PROPERTY_MAP
/* 131 */           .optionalFieldOf("properties", PropertyMap.EMPTY).forGetter(Partial::properties))
/* 132 */         .apply(i, Partial::new));
/*     */     
/* 134 */     public static final StreamCodec<ByteBuf, Partial> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.PLAYER_NAME
/* 135 */         .apply(ByteBufCodecs::optional), Partial::name, UUIDUtil.STREAM_CODEC
/* 136 */         .apply(ByteBufCodecs::optional), Partial::id, ByteBufCodecs.GAME_PROFILE_PROPERTIES, Partial::properties, Partial::new);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 142 */     private GameProfile createProfile() { return ResolvableProfile.createPartialProfile(this.name, this.id, this.properties); } }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final class Static
/*     */     extends ResolvableProfile
/*     */   {
/* 150 */     public static final Static EMPTY = new Static(Either.right(ResolvableProfile.Partial.EMPTY), PlayerSkin.Patch.EMPTY);
/*     */     
/*     */     private final Either<GameProfile, ResolvableProfile.Partial> contents;
/*     */     
/*     */     private Static(Either<GameProfile, ResolvableProfile.Partial> contents, PlayerSkin.Patch skinPatch) {
/* 155 */       super((GameProfile)contents.map(gameProfile -> 
/* 156 */             gameProfile, ResolvableProfile.Partial::createProfile), skinPatch);
/*     */ 
/*     */ 
/*     */       
/* 160 */       this.contents = contents;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 165 */     public CompletableFuture<GameProfile> resolveProfile(ProfileResolver profileResolver) { return CompletableFuture.completedFuture(this.partialProfile); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 170 */     protected Either<GameProfile, ResolvableProfile.Partial> unpack() { return this.contents; }
/*     */ 
/*     */ 
/*     */     
/*     */     public Optional<String> name() {
/* 175 */       return (Optional)this.contents.map(gameProfile -> 
/* 176 */           Optional.of(gameProfile.name()), partial -> 
/* 177 */           partial.name);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 183 */     public boolean equals(Object o) { if (this != o) { if (o instanceof Static) { Static that = (Static)o; if (this.contents.equals(that.contents) && this.skinPatch.equals(that.skinPatch)); }  return false; }
/*     */        }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 188 */       result = 31 + this.contents.hashCode();
/* 189 */       return 31 * result + this.skinPatch.hashCode();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void addToTooltip(Item.TooltipContext context, Consumer<Component> consumer, TooltipFlag flag, DataComponentGetter components) {}
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final class Dynamic
/*     */     extends ResolvableProfile
/*     */   {
/* 204 */     private static final Component DYNAMIC_TOOLTIP = Component.translatable("component.profile.dynamic").withStyle(ChatFormatting.GRAY);
/*     */     private final Either<String, UUID> nameOrId;
/*     */     
/*     */     private Dynamic(Either<String, UUID> nameOrId, PlayerSkin.Patch skinPatch) {
/* 208 */       super(ResolvableProfile.createPartialProfile(nameOrId.left(), nameOrId.right(), PropertyMap.EMPTY), skinPatch);
/* 209 */       this.nameOrId = nameOrId;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 215 */     public Optional<String> name() { return this.nameOrId.left(); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 220 */     public boolean equals(Object o) { if (this != o) { if (o instanceof Dynamic) { Dynamic that = (Dynamic)o; if (this.nameOrId.equals(that.nameOrId) && this.skinPatch.equals(that.skinPatch)); }  return false; }
/*     */        }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 225 */       result = 31 + this.nameOrId.hashCode();
/* 226 */       return 31 * result + this.skinPatch.hashCode();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 232 */     protected Either<GameProfile, ResolvableProfile.Partial> unpack() { return Either.right(new ResolvableProfile.Partial(this.nameOrId.left(), this.nameOrId.right(), PropertyMap.EMPTY)); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 237 */     public CompletableFuture<GameProfile> resolveProfile(ProfileResolver profileResolver) { return CompletableFuture.supplyAsync(() -> (GameProfile)profileResolver.fetchByNameOrId(this.nameOrId).orElse(this.partialProfile), Util.nonCriticalIoPool()); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 242 */     public void addToTooltip(Item.TooltipContext context, Consumer<Component> consumer, TooltipFlag flag, DataComponentGetter components) { consumer.accept(DYNAMIC_TOOLTIP); }
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\item\component\ResolvableProfile.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */