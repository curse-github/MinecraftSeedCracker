/*     */ package net.minecraft.world.item.component;
/*     */ 
/*     */ import com.mojang.authlib.GameProfile;
/*     */ import com.mojang.datafixers.util.Either;
/*     */ import java.util.Optional;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.function.Consumer;
/*     */ import net.minecraft.core.component.DataComponentGetter;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.server.players.ProfileResolver;
/*     */ import net.minecraft.world.entity.player.PlayerSkin;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.TooltipFlag;
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
/*     */ 
/*     */ public final class Static
/*     */   extends ResolvableProfile
/*     */ {
/* 150 */   public static final Static EMPTY = new Static(Either.right(ResolvableProfile.Partial.EMPTY), PlayerSkin.Patch.EMPTY);
/*     */   
/*     */   private final Either<GameProfile, ResolvableProfile.Partial> contents;
/*     */   
/*     */   private Static(Either<GameProfile, ResolvableProfile.Partial> contents, PlayerSkin.Patch skinPatch) {
/* 155 */     super((GameProfile)contents.map(gameProfile -> 
/* 156 */           gameProfile, ResolvableProfile.Partial::createProfile), skinPatch);
/*     */ 
/*     */ 
/*     */     
/* 160 */     this.contents = contents;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 165 */   public CompletableFuture<GameProfile> resolveProfile(ProfileResolver profileResolver) { return CompletableFuture.completedFuture(this.partialProfile); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 170 */   protected Either<GameProfile, ResolvableProfile.Partial> unpack() { return this.contents; }
/*     */ 
/*     */ 
/*     */   
/*     */   public Optional<String> name() {
/* 175 */     return (Optional)this.contents.map(gameProfile -> 
/* 176 */         Optional.of(gameProfile.name()), partial -> 
/* 177 */         partial.name);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 183 */   public boolean equals(Object o) { if (this != o) { if (o instanceof Static) { Static that = (Static)o; if (this.contents.equals(that.contents) && this.skinPatch.equals(that.skinPatch)); }  return false; }
/*     */      }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 188 */     result = 31 + this.contents.hashCode();
/* 189 */     return 31 * result + this.skinPatch.hashCode();
/*     */   }
/*     */   
/*     */   public void addToTooltip(Item.TooltipContext context, Consumer<Component> consumer, TooltipFlag flag, DataComponentGetter components) {}
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\item\component\ResolvableProfile$Static.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */