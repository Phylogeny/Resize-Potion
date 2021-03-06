package com.camellias.resizer.network.packets;

import com.camellias.resizer.Main;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class ShrinkingPacket implements IMessage
{
	public ShrinkingPacket()
	{
		
	}
	
	public int playerID;
	public int duration;
	public int amplifier;
	
	public ShrinkingPacket(EntityPlayer player, int duration, int amplifier)
	{
		this.playerID = player.getEntityId();
		this.duration = duration;
		this.amplifier = amplifier;
	}
	
	@Override
	public void toBytes(ByteBuf buf)
	{
		buf.writeInt(playerID);
		buf.writeInt(duration);
		buf.writeInt(amplifier);
	}
	
	@Override
	public void fromBytes(ByteBuf buf)
	{
		this.playerID = buf.readInt();
		this.duration = buf.readInt();
		this.amplifier = buf.readInt();
	}
	
//-------------------------------------------------------------------------------------------------------------------------//
	
	public static class ShrinkingPacketHandler implements IMessageHandler<ShrinkingPacket, IMessage>
	{
		@Override
		public IMessage onMessage(ShrinkingPacket message, MessageContext ctx)
		{
			Main.proxy.getThreadListener(ctx).addScheduledTask(() ->
			{
				if(Main.proxy.getPlayer(ctx) != null)
				{
					EntityPlayer player = (EntityPlayer) Main.proxy.getPlayer(ctx).world.getEntityByID(message.playerID);
					
					player.removePotionEffect(Main.GROWTH);
					player.addPotionEffect(new PotionEffect(Main.SHRINKING, message.duration, message.amplifier));
				}
			});
			
			return null;
		}
	}
}
