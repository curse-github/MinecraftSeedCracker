/*    */ package net.minecraft.commands.arguments;
/*    */ 
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.commands.arguments.selector.EntitySelector;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.waypoints.WaypointTransmitter;
/*    */ 
/*    */ public class WaypointArgument {
/* 13 */   public static final SimpleCommandExceptionType ERROR_NOT_A_WAYPOINT = new SimpleCommandExceptionType(Component.translatable("argument.waypoint.invalid"));
/*    */   
/*    */   public static WaypointTransmitter getWaypoint(CommandContext<CommandSourceStack> context, String name) throws CommandSyntaxException {
/* 16 */     Entity singleEntity = ((EntitySelector)context.getArgument(name, EntitySelector.class)).findSingleEntity((CommandSourceStack)context.getSource());
/* 17 */     if (singleEntity instanceof WaypointTransmitter) return (WaypointTransmitter)singleEntity;
/*    */ 
/*    */     
/* 20 */     throw ERROR_NOT_A_WAYPOINT.create();
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\commands\arguments\WaypointArgument.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */