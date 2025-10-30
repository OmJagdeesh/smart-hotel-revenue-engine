const express = require('express');
const User = require('../models/User');
const router = express.Router();

// Create new user
router.post('/', async (req, res) => {
  try {
    const { name, bio, contact, password } = req.body;
    const user = new User({ name, bio, contact, password });
    await user.save();
    res.json(user);
  } catch (err) {
    res.status(500).json({ message: err.message });
  }
});

// Get all users
router.get('/', async (req, res) => {
  const users = await User.find();
  res.json(users);
});

// Get single user
router.get('/:id', async (req, res) => {
  const user = await User.findById(req.params.id);
  res.json(user);
});

// Update user
router.put('/:id', async (req, res) => {
  const { name, bio, contact } = req.body;
  const user = await User.findByIdAndUpdate(req.params.id, { name, bio, contact }, { new: true });
  res.json(user);
});

// Delete user
router.delete('/:id', async (req, res) => {
  await User.findByIdAndDelete(req.params.id);
  res.json({ message: 'User deleted' });
});

module.exports = router;
