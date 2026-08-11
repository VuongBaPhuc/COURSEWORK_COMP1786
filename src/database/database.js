import AsyncStorage from '@react-native-async-storage/async-storage';

const STORAGE_KEY = 'mhike_hikes';

export async function loadHikes() {
  try {
    const value = await AsyncStorage.getItem(STORAGE_KEY);
    if (!value) return [];
    return JSON.parse(value);
  } catch (error) {
    console.error('Failed to load hikes', error);
    return [];
  }
}

export async function saveHikes(hikes) {
  try {
    await AsyncStorage.setItem(STORAGE_KEY, JSON.stringify(hikes));
  } catch (error) {
    console.error('Failed to save hikes', error);
  }
}
