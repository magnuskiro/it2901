/* -*- Mode:C++; c-file-style:"gnu"; indent-tabs-mode:nil; -*- */
/*
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 2 as
 * published by the Free Software Foundation;
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
 
// Network topology
// //
// //            ESB      Client Client  Client
// //             |         |       |       |       
// //             ===========================
// //                         LAN
// //


#include <iostream>
#include <fstream>

#include "ns3/core-module.h"
#include "ns3/csma-module.h"
#include "ns3/point-to-point-module.h"
#include "ns3/internet-module.h"
#include "ns3/ipv4-address-helper.h"
#include "ns3/stats-module.h"
#include "ns3/tap-bridge-helper.h"
#include "ns3/ipv4-global-routing-helper.h"

using namespace ns3;
using std::stringstream;
using std::string;

NS_LOG_COMPONENT_DEFINE ("QoS System test 1");

void printTime(int interval) 
{
    Ptr<RealtimeSimulatorImpl> impl = DynamicCast<RealtimeSimulatorImpl>(Simulator::GetImplementation ()); 
    Time real_time = impl->RealtimeNow();

    Time sim_time = Simulator::Now();

    std::cout << "drift:" << real_time.GetMilliSeconds() << ":" << sim_time.GetMilliSeconds() << ":" << (real_time.GetMilliSeconds() - sim_time.GetMilliSeconds()) << std::endl;
    Simulator::Schedule(Seconds(interval), &printTime, interval);
}

int main (int argc, char *argv[])
{
    CommandLine cmd;
    uint32_t run_time= 200, seed = 1, n = 3, mtu = 0;
    string runID;
    string dataRate;
    cmd.AddValue ("duration", "Duration of simulation", run_time);
    cmd.AddValue ("seed", "Seed for the Random generator", seed);
    cmd.AddValue ("runID", "Identity of this run", runID);
    cmd.AddValue("datarate", "Data rate for LAN", dataRate);
    cmd.AddValue("nodes", "Not used in this test", n);
    cmd.AddValue("mtu", "MTU used for TapBridges", mtu);
    
    cmd.Parse(argc, argv);
    
    stringstream s;
    s << "Duration: " << run_time << ", Seed: " << seed << ", runID: " << runID << ", DataRate: " << dataRate;
    std::cout << s.str() << std::endl;

    SeedManager::SetSeed (seed);
    GlobalValue::Bind ("SimulatorImplementationType", StringValue ("ns3::RealtimeSimulatorImpl"));
    GlobalValue::Bind ("ChecksumEnabled", BooleanValue (true));
    
    NodeContainer nodes;
    nodes.Create(4);
    
    CsmaHelper csma;
    csma.SetChannelAttribute ("DataRate", StringValue (dataRate));
    
    NetDeviceContainer csmaDevices;
    csmaDevices = csma.Install (nodes);
    
    TapBridgeHelper tapBridge;
    tapBridge.SetAttribute ("Mode", StringValue ("UseLocal"));
    tapBridge.SetAttribute ("Mtu", UintegerValue(mtu)); 
    
    //Tap bridge setup for ESB
    std::cout << "Adding tap bridge: tap-1\n";
    tapBridge.SetAttribute ("DeviceName", StringValue ("tap-1"));
    tapBridge.Install (nodes.Get(0), csmaDevices.Get(0));
    
    //Tap bridge setup for client 1
    std::cout << "Adding tap bridge: tap-2\n";
    tapBridge.SetAttribute ("DeviceName", StringValue ("tap-2"));
    tapBridge.Install (nodes.Get(1), csmaDevices.Get(1));
    
    //Tap bridge setup for client 2
    std::cout << "Adding tap bridge: tap-3\n";
    tapBridge.SetAttribute ("DeviceName", StringValue ("tap-3"));
    tapBridge.Install (nodes.Get(2), csmaDevices.Get(2));

    //Tap bridge setup for client 3
    std::cout << "Adding tap bridge: tap-4\n";
    tapBridge.SetAttribute ("DeviceName", StringValue ("tap-4"));
    tapBridge.Install (nodes.Get(3), csmaDevices.Get(3));
    
    
    
    std::cout << "Starting simulation. Will run for " << run_time << " seconds...\n";  
    Simulator::Schedule(Seconds(0), &printTime, 1);
    Simulator::Stop (Seconds (run_time));

    Simulator::Run();

    Simulator::Destroy ();

    std::cout << "Done.\n";
}

