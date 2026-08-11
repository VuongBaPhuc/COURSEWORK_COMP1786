import React, { useEffect, useState } from 'react';
import { NavigationContainer } from '@react-navigation/native';
import { createNativeStackNavigator } from '@react-navigation/native-stack';
import { StatusBar } from 'expo-status-bar';
import HomeScreen from './src/screens/HomeScreen';
import HikeListScreen from './src/screens/HikeListScreen';
import HikeDetailScreen from './src/screens/HikeDetailScreen';
import AddHikeScreen from './src/screens/AddHikeScreen';
import EditHikeScreen from './src/screens/EditHikeScreen';
import { loadHikes, saveHikes } from './src/database/database';

const Stack = createNativeStackNavigator();

export default function App() {
  const [hikes, setHikes] = useState([]);
  const [isReady, setIsReady] = useState(false);

  useEffect(() => {
    async function initialize() {
      const storedHikes = await loadHikes();
      setHikes(storedHikes);
      setIsReady(true);
    }
    initialize();
  }, []);

  useEffect(() => {
    if (isReady) {
      saveHikes(hikes);
    }
  }, [hikes, isReady]);

  const addHike = (newHike) => {
    setHikes((current) => [...current, newHike]);
  };

  const updateHike = (updatedHike) => {
    setHikes((current) => current.map((hike) => hike.id === updatedHike.id ? updatedHike : hike));
  };

  const deleteHike = (hikeId) => {
    setHikes((current) => current.filter((hike) => hike.id !== hikeId));
  };

  return (
    <NavigationContainer>
      <StatusBar style="auto" />
      <Stack.Navigator initialRouteName="Home">
        <Stack.Screen name="Home" options={{ title: 'MHike Home' }}>
          {(props) => <HomeScreen {...props} />}
        </Stack.Screen>
        <Stack.Screen name="HikeList" options={{ title: 'Hike List' }}>
          {(props) => <HikeListScreen {...props} hikes={hikes} />}
        </Stack.Screen>
        <Stack.Screen name="HikeDetail" options={{ title: 'Hike Details' }}>
          {(props) => <HikeDetailScreen {...props} hikes={hikes} onDelete={deleteHike} />}
        </Stack.Screen>
        <Stack.Screen name="AddHike" options={{ title: 'Add New Hike' }}>
          {(props) => <AddHikeScreen {...props} onAddHike={addHike} />}
        </Stack.Screen>
        <Stack.Screen name="EditHike" options={{ title: 'Edit Hike' }}>
          {(props) => <EditHikeScreen {...props} hikes={hikes} onUpdateHike={updateHike} />}
        </Stack.Screen>
      </Stack.Navigator>
    </NavigationContainer>
  );
}
